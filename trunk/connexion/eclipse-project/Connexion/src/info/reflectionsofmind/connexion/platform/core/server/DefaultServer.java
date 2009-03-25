package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.fortress.core.common.exception.GameTurnException;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessageDecoder;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Action;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Chat;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_ConnectionRequest;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_DisconnectNotice;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.ICTSMessageTarget;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGame;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerInitInfo;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.core.transport.IClientPacket;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultServer implements IServer, ICTSMessageTarget, Participant.IStateListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final IApplication application;

	private IServerGameFactory<?> gameFactory;
	private IServerGame<IServerInitInfo, IChange, IAction, IServerGame.IListener> game;

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
	public synchronized void onPacket(IClientPacket packet)
	{
		CTSMessageDecoder.decode(packet.getContents()).dispatch(packet.getFrom(), this);
	}

	@Override
	public synchronized void onError(TransportException exception)
	{
		exception.printStackTrace();
	}

	@Override
	public synchronized void onBeforeStopped(IServerTransport transport)
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
	// === STC MESSAGE DISPATCH HANDLERS
	// ============================================================================================

	@Override
	public synchronized void onConnectionRequest(IClientNode from, CTSMessage_ConnectionRequest event)
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
	public synchronized void onDisconnectNotice(IClientNode from, CTSMessage_DisconnectNotice event)
	{
		final IRemoteClient disconnectedClient = ServerUtil.getClientByNode(this, from);
		disconnect(disconnectedClient, event.getReason());
	}

	@Override
	public synchronized void onChatMessage(IClientNode from, CTSMessage_Chat event)
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
	public void onAction(IClientNode from, CTSMessage_Action event)
	{
		this.game.onAction(this.game.getCoder().decodeAction(event.getEncodedAction()));
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
			if (client != disconnectedClient)
				client.sendDisconnected(this, disconnectedClient, reason);
		}

		this.clients.remove(disconnectedClient);

		for (IServer.IListener listener : this.listeners)
		{
			listener.onClientDisconnected(disconnectedClient);
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
	public void sendChat(String message)
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
