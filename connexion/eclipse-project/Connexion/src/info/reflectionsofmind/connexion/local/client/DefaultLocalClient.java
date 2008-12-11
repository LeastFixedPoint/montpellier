package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.event.stc.IServerToClientEventListener;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerRejectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_SpectatorAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_SpectatorRejectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultLocalClient implements IClient, IServerToClientEventListener
{
	private final Settings settings;
	private final List<ITransport> transports = new ArrayList<ITransport>();
	private IRemoteServer server;
	private ITileSource tileSource;
	private RemoteTileSequence sequence;
	private final List<Client> clients = new ArrayList<Client>();
	private Client client;
	private Game game;

	public DefaultLocalClient(final Settings settings)
	{
		this.settings = settings;
		
		this.transports.add(new JabberTransport(settings.getJabberAddress()));
	}
	
	// ============================================================================================
	// === COMMANDS
	// ============================================================================================
	
	public void connect(final IRemoteServer server)
	{
		this.server = server;
		this.server.addListener(this);

		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}

		try
		{
			this.server.sendEvent(new ClientToServer_ClientConnectionRequestEvent(getName()));
		}
		catch (ServerConnectionException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (RemoteServerException exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	public void disconnect(DisconnectReason reason)
	{
		try
		{
			this.server.sendEvent(new ClientToServer_DisconnectNoticeEvent(reason));
		}
		catch (ServerConnectionException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (RemoteServerException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================
	
	@Override
	public synchronized void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event)
	{
		for (String clientName : event.getExistingPlayers())
		{
			this.clients.add(new Client(clientName));
		}
		
		this.client = this.clients.get(this.clients.size() - 1);
	}
	
	@Override
	public synchronized void onClientConnected(ServerToClient_ClientConnectedEvent event)
	{
		this.clients.add(new Client(event.getClientName()));
	}
	
	@Override
	public synchronized void onClientDisconnected(ServerToClient_ClientDisconnectedEvent event)
	{
		this.clients.get(event.getClientIndex()).setDisconnected(event.getReason());
	}
	
	@Override
	public void onPlayerAccepted(ServerToClient_PlayerAcceptedEvent event)
	{
		this.clients.get(event.getClientIndex()).setAccepted();
	}
	
	@Override
	public void onPlayerRejected(ServerToClient_PlayerRejectedEvent event)
	{
		this.clients.get(event.getClientIndex()).setRejected();
	}
	
	@Override
	public void onSpectatorAccepted(ServerToClient_SpectatorAcceptedEvent event)
	{
		this.clients.get(event.getClientIndex()).setSpectator();
	}
	
	@Override
	public void onSpectatorRejected(ServerToClient_SpectatorRejectedEvent event)
	{
		this.clients.get(event.getClientIndex()).setRejected();
	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{
		try
		{
			this.sequence = new RemoteTileSequence(event.getTotalNumberOfTiles());
			this.sequence.setCurrentTile(new Tile(event.getInitialTileCode()));
			
			final List<Player> players = new ArrayList<Player>();

			for (Client client : this.clients)
			{
				players.add(new Player(client.getName()));
			}

			this.game = new Game(event.getGameName(), this.sequence, players);

			final Turn turn = new Turn(false);
			final IGeometry geometry = this.game.getBoard().getGeometry();
			turn.addTilePlacement(geometry.getInitialLocation(), geometry.getDirections().get(0));

			this.game.doTurn(turn);
			this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{
		if (event.getCurrentTileCode() == null)
		{
			this.sequence.setNoMoreTiles();
		}

		if (event.getTurn() != null) // Do not double-process our own turns.
		{
			try
			{
				this.game.doTurn(event.getTurn());
			}
			catch (GameTurnException exception)
			{
				throw new RuntimeException(exception);
			}
		}
		else
		{
			this.game.endTurn(true);
		}

		if (event.getCurrentTileCode() != null)
		{
			try
			{
				this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
			}
			catch (TileCodeFormatException exception)
			{
				throw new RuntimeException(exception);
			}
		}
	}
	
	@Override
	public void onMessage(ServerToClient_MessageEvent event)
	{
		
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	@Override
	public String getName()
	{
		return this.settings.getPlayerName();
	}

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
	public IRemoteServer getRemoteServer()
	{
		return this.server;
	}

	@Override
	public List<Client> getClients()
	{
		return this.clients;
	}
	
	@Override
	public Client getClient()
	{
		return this.client;
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

	// ============================================================================================
	// === TILE SEQUENCE PROXY
	// ============================================================================================

	private final class RemoteTileSequence implements ITileSequence
	{
		private Tile currentTile = null;
		private boolean hasMoreTiles = true;
		private final Integer totalNumberOfTiles;

		public RemoteTileSequence(Integer totalNumberOfTiles)
		{
			this.totalNumberOfTiles = totalNumberOfTiles;
		}

		public void setCurrentTile(final Tile tile)
		{
			this.currentTile = tile;
		}

		public void setNoMoreTiles()
		{
			this.hasMoreTiles = false;
		}
		
		@Override
		public Tile getCurrentTile()
		{
			return this.currentTile;
		}

		@Override
		public void nextTile()
		{
			this.currentTile = null;
		}

		@Override
		public boolean hasMoreTiles()
		{
			return this.hasMoreTiles;
		}

		@Override
		public Integer getTotalNumberOfTiles()
		{
			return this.totalNumberOfTiles;
		}
	}
}
