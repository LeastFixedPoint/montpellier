package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultLocalClient implements IClient, IRemoteServer.IListener
{
	// All of this we have always
	private final String name;

	// All of this we have after connect() call
	private IRemoteServer server;
	private ITileSource tileSource;

	// All of this we have after onConnectionAccepted() event
	private final List<Player> players = new ArrayList<Player>();
	private Player player;

	// All of this we have after onStart() event
	private RemoteTileSequence sequence;
	private Game game;

	public DefaultLocalClient(final String name)
	{
		this.name = name;
	}
	
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
			this.server.connect(this);
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
		for (String playerName : event.getExistingPlayers())
		{
			this.players.add(new Player(playerName));
		}
		
		this.player = this.players.get(this.players.size());
	}
	
	@Override
	public synchronized void onPlayerConnect(ServerToClient_PlayerConnectedEvent event)
	{
		this.players.add(new Player(event.getPlayerName()));
	}
	
	@Override
	public synchronized void onPlayerDisconnect(ServerToClient_PlayerDisconnectedEvent event)
	{
		this.players.remove(event.getPlayerIndex());
	}

	@Override
	public synchronized void onStart(final ServerToClient_GameStartedEvent event)
	{
		try
		{
			this.sequence = new RemoteTileSequence(event.getTotalNumberOfTiles());
			this.sequence.setCurrentTile(new Tile(event.getInitialTileCode()));
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
	public synchronized void onTurn(final ServerToClient_TurnEvent event)
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
		return this.name;
	}

	@Override
	public Player getPlayer()
	{
		return this.player;
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
	public IRemoteServer getServer()
	{
		return this.server;
	}
	
	@Override
	public List<Player> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
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
