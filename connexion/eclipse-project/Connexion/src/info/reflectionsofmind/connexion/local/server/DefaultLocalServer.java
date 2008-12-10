package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEventDecoder;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.cts.IClientToServerEventListener;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.RemoteClient;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;
import info.reflectionsofmind.connexion.transport.local.ServerLocalTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultLocalServer implements IServer, ITransport.IListener, IClientToServerEventListener
{
	private final List<ITransport> transports = new ArrayList<ITransport>();
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final Settings settings;

	private Game game;
	private ITileSource tileSource;

	public DefaultLocalServer(final Settings settings)
	{
		this.settings = settings;

		this.transports.add(new ServerLocalTransport(this));
		this.transports.add(new JabberTransport(settings.getJabberAddress()));
	}

	// ============================================================================================
	// === HANDLERS
	// ============================================================================================

	@Override
	public void onTransportMessage(INode origin, String message)
	{
		ClientToServerEventDecoder.decode(message).dispatch(origin, this);
	}

	@Override
	public void onClientConnectionRequestEvent(INode origin, ClientToServer_ClientConnectionRequestEvent event)
	{
		final RemoteClient newRemoteClient = new RemoteClient(origin, event.getPlayerName());
		this.clients.add(newRemoteClient);

		try
		{
			newRemoteClient.sendEvent(new ServerToClient_ConnectionAcceptedEvent(this));
			newRemoteClient.setConnected();
			fireEvent(new ServerToClient_ClientConnectedEvent(newRemoteClient));
		}
		catch (TransportException exception)
		{
			disconnect(newRemoteClient, DisconnectReason.CONNECTION_FAILURE);
		}
	}

	@Override
	public void onDisconnectNoticeEvent(INode from, ClientToServer_DisconnectNoticeEvent event)
	{
		final IRemoteClient client = ServerUtil.getClientByNode(this, from);
		client.setDisconnected(DisconnectReason.CLIENT_REQUEST);
		fireEvent(new ServerToClient_ClientDisconnectedEvent(this, client, DisconnectReason.CLIENT_REQUEST));
	}

	@Override
	public void onMessageEvent(INode from, ClientToServer_MessageEvent event)
	{
		final int index = getClients().indexOf(ServerUtil.getClientByNode(this, from));
		fireEvent(new ServerToClient_MessageEvent(index, event.getMessage()));
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
		
		fireEvent(new ServerToClient_TurnEvent(event.getTurn(), getGame().getCurrentTile().getCode()));
	}

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	private void fireEvent(ServerToClientEvent event)
	{
		for (IRemoteClient remoteClient : this.clients)
		{
			if (!IRemoteClient.State.isConnected(remoteClient.getState())) continue;

			try
			{
				remoteClient.sendEvent(event);
			}
			catch (TransportException exception)
			{
				disconnect(remoteClient, DisconnectReason.CONNECTION_FAILURE);
			}
		}
	}

	private void disconnect(IRemoteClient disconnectedClient, DisconnectReason reason)
	{
		disconnectedClient.setDisconnected(reason);
		fireEvent(new ServerToClient_ClientDisconnectedEvent(this, disconnectedClient, reason));
	}

	private void createGame(final String name)
	{
		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));

			final List<Tile> tiles = new ArrayList<Tile>();

			for (final TileData tileData : this.tileSource.getTiles())
			{
				tiles.add(new Tile(tileData.getCode()));
			}

			this.game = new Game(name, new RandomTileSequence(tiles), ServerUtil.getPlayers(this));
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
	public synchronized void startGame(final String name)
	{
		createGame(name);

		try
		{
			placeInitialTile();
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}

		fireEvent(new ServerToClient_GameStartedEvent(getGame()));
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
