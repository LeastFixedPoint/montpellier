package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.common.event.cts.ClientToServerEventDecoder;
import info.reflectionsofmind.connexion.common.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.common.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.common.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.common.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.common.event.cts.IClientToServerEventListener;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;
import info.reflectionsofmind.connexion.transport.local.ServerLocalTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultLocalServer implements IServer, ITransport.IListener, IClientToServerEventListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final List<ITransport> transports = new ArrayList<ITransport>();
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final Settings settings;

	private Game game;
	private ITileSource tileSource;

	public DefaultLocalServer(final Settings settings)
	{
		this.settings = settings;

		this.transports.add(new ServerLocalTransport(settings));
		this.transports.add(new JabberTransport(settings.getJabberAddress()));
		
		for (ITransport transport : this.transports)
		{
			transport.addListener(this);
		}
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
	public void onTransportMessage(INode origin, String message)
	{
		ClientToServerEventDecoder.decode(message).dispatch(origin, this);
	}

	// ============================================================================================
	// === STC EVENT DISPATCH HANDLERS
	// ============================================================================================

	@Override
	public void onClientConnectionRequestEvent(INode origin, ClientToServer_ClientConnectionRequestEvent event)
	{
		final IRemoteClient newRemoteClient = new RemoteClient(new Client(event.getPlayerName()), origin);

		newRemoteClient.sendConnectionAccepted(this);

		this.clients.add(newRemoteClient);
		
		for (IRemoteClient client : getClients())
		{
			client.sendConnected(this, newRemoteClient);
		}

		for (IServer.IListener listener : this.listeners)
		{
			listener.onClientConnected(newRemoteClient);
		}
	}

	@Override
	public void onDisconnectNoticeEvent(INode from, ClientToServer_DisconnectNoticeEvent event)
	{
		final IRemoteClient disconnectedClient = ServerUtil.getClientByNode(this, from);
		disconnect(disconnectedClient, event.getReason());
	}

	@Override
	public void onMessageEvent(INode from, ClientToServer_ChatMessageEvent event)
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
	public void onTurnEvent(INode from, ClientToServer_TurnEvent event)
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
	// === IMPLEMENTATION
	// ====================================================================================================

	public void disconnect(IRemoteClient disconnectedClient, DisconnectReason reason)
	{
		this.clients.remove(disconnectedClient);

		for (IRemoteClient client : getClients())
		{
			client.sendDisconnected(this, disconnectedClient, reason);
		}

		for (IServer.IListener listener : this.listeners)
		{
			listener.onClientDisconnected(disconnectedClient);
		}
	}

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
				players.add(new Player(client.getClient().getName()));
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
	public List<ITransport> getTransports()
	{
		return ImmutableList.copyOf(this.transports);
	}

	@Override
	public Settings getSettings()
	{
		return this.settings;
	}

	@Override
	public List<IRemoteClient> getClients()
	{
		return this.clients;
	}
}
