package info.reflectionsofmind.connexion.platform.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.common.Participant;
import info.reflectionsofmind.connexion.platform.common.Participant.State;
import info.reflectionsofmind.connexion.platform.common.event.cts.ClientToServerEventDecoder;
import info.reflectionsofmind.connexion.platform.common.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.platform.common.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.platform.common.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.platform.common.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.platform.common.event.cts.IClientToServerEventListener;
import info.reflectionsofmind.connexion.platform.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.transport.IClientPacket;
import info.reflectionsofmind.connexion.platform.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.transport.TransportException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultServer implements IServer, IClientToServerEventListener, Participant.IStateListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final IApplication application;

	private Game game;
	private ITileSource tileSource;

	public DefaultServer(IApplication application)
	{
		this.application = application;
	}

	// ====================================================================================================
	// === LISTENER
	// ====================================================================================================

	public void addListener(IListener listener)
	{
		this.listeners.add(listener);
	}

	// ============================================================================================
	// === TRANSPORT MESSAGE HANDLER
	// ============================================================================================

	@Override
	public void onPacket(IClientPacket packet)
	{
		ClientToServerEventDecoder.decode(packet.getContents()).dispatch(packet.getFrom(), this);
	}

	@Override
	public void onError(TransportException exception)
	{
		exception.printStackTrace();
	}

	@Override
	public void onBeforeStopped(IServerTransport transport)
	{
		for (IRemoteClient client : getClients())
		{
			if (client.getNode().getTransport() == transport)
			{
				disconnect(client, DisconnectReason.CONNECTION_FAILURE);
			}
		}
	}

	// ============================================================================================
	// === STC EVENT DISPATCH HANDLERS
	// ============================================================================================

	@Override
	public void onClientConnectionRequestEvent(IClientNode from, ClientToServer_ClientConnectionRequestEvent event)
	{
		final IRemoteClient newRemoteClient = new RemoteClient(new Participant(event.getPlayerName()), from);

		this.clients.add(newRemoteClient);

		newRemoteClient.sendConnectionAccepted(this);

		for (IRemoteClient client : getClients())
		{
			if (client != newRemoteClient)
			{
				client.sendConnected(this, newRemoteClient);
			}
		}

		for (IServer.IListener listener : this.listeners)
		{
			listener.onClientConnected(newRemoteClient);
		}

		newRemoteClient.getParticipant().addStateListener(this);
	}

	@Override
	public void onDisconnectNoticeEvent(IClientNode from, ClientToServer_DisconnectNoticeEvent event)
	{
		final IRemoteClient disconnectedClient = ServerUtil.getClientByNode(this, from);
		disconnect(disconnectedClient, event.getReason());
	}

	@Override
	public void onMessageEvent(IClientNode from, ClientToServer_ChatMessageEvent event)
	{
		final IRemoteClient client = ServerUtil.getClientByNode(this, from);

		for (IRemoteClient otherClient : getClients())
		{
			if (otherClient != client)
			{
				otherClient.sendChatMessage(this, client, event.getMessage());
			}
		}

		for (IServer.IListener listener : this.listeners)
		{
			listener.onClientMessage(client, event.getMessage());
		}
	}

	@Override
	public void onTurnEvent(IClientNode from, ClientToServer_TurnEvent event)
	{
		final IRemoteClient client = ServerUtil.getClientByNode(this, from);

		try
		{
			this.game.doTurn(event.getTurn());
		}
		catch (final GameTurnException exception)
		{
			disconnect(client, DisconnectReason.DESYNCHRONIZATION);
			return;
		}

		for (IRemoteClient otherClient : getClients())
		{
			if (otherClient != client)
			{
				client.sendLastTurn(this);
			}
		}
	}

	// ====================================================================================================
	// === CLIENT STATE LISTENER
	// ====================================================================================================

	@Override
	public void onAfterClientStateChange(Participant client, State previousState)
	{
		for (IRemoteClient remoteClient : getClients())
		{
			remoteClient.sendStateChanged(this, ServerUtil.getClient(this, client), previousState);
		}
	}

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	private void createGame()
	{
		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));

			final List<Tile> tiles = new ArrayList<Tile>();

			for (final TileData tileData : this.tileSource.getTiles())
			{
				tiles.add(new Tile(tileData.getCode()));
			}

			final List<Player> players = new ArrayList<Player>();

			for (IRemoteClient client : getClients())
			{
				players.add(new Player(client.getParticipant().getName()));
			}

			this.game = new Game(new RandomTileSequence(tiles), players);
		}
		catch (final IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (final TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	private void placeInitialTile() throws GameTurnException
	{
		final Turn turn = new Turn(false);
		final IGeometry geometry = getGame().getBoard().getGeometry();
		turn.addTilePlacement(geometry.getInitialLocation(), geometry.getDirections().get(0));
		getGame().doTurn(turn);
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	@Override
	public void disconnect(IRemoteClient disconnectedClient, DisconnectReason reason)
	{
		for (IRemoteClient client : getClients())
		{
			if (client != disconnectedClient) client.sendDisconnected(this, disconnectedClient, reason);
		}

		this.clients.remove(disconnectedClient);

		for (IServer.IListener listener : this.listeners)
		{
			listener.onAfterClientDisconnected(disconnectedClient);
		}
	}

	@Override
	public synchronized void startGame()
	{
		createGame();

		for (IRemoteClient client : getClients())
		{
			client.sendGameStarted(this);
		}

		try
		{
			placeInitialTile();
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}

		for (IRemoteClient client : getClients())
		{
			client.sendLastTurn(this);
		}
	}

	@Override
	public void sendMessage(String message)
	{
		for (IRemoteClient client : getClients())
		{
			client.sendChatMessage(this, null, message);
		}
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	@Override
	public Game getGame()
	{
		return this.game;
	}

	@Override
	public ITileSource getTileSource()
	{
		return this.tileSource;
	}

	@Override
	public IApplication getApplication()
	{
		return this.application;
	}

	@Override
	public List<IRemoteClient> getClients()
	{
		return ImmutableList.copyOf(this.clients);
	}
}
