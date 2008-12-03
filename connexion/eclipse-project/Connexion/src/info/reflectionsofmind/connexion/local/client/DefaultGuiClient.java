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
import info.reflectionsofmind.connexion.local.client.exception.DesynchronizationException;
import info.reflectionsofmind.connexion.local.client.gui.play.GameWindow;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.awt.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class DefaultGuiClient implements IClient, IRemoteServer.IListener
{
	// All of this we have always
	private final IRemoteServer server;
	private final String name;
	private final ConnectionFrame conectionFrame;
	private final ITileSource tileSource;

	// All of this we have after onConnectionAccepted
	private final List<Player> players = new ArrayList<Player>();
	private Player player;

	// All of this we have after onStart
	private RemoteTileSequence sequence;
	private GameWindow clientUI;
	private Game game;

	public DefaultGuiClient(final IRemoteServer server, final String name)
	{
		this.server = server;
		this.name = name;

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

		this.conectionFrame = new ConnectionFrame(name);
		this.conectionFrame.setVisible(true);
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================
	
	@Override
	public void connect(IRemoteServer server)
	{
	}
	
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
	public void onMessage(ServerToClient_MessageEvent event)
	{
	}
	
	@Override
	public synchronized void onPlayerConnected(ServerToClient_PlayerConnectedEvent event)
	{
		this.players.add(new Player(event.getPlayerName()));
	}
	
	@Override
	public synchronized void onPlayerDisconnected(ServerToClient_PlayerDisconnectedEvent event)
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

		this.conectionFrame.dispose();

		this.clientUI = new GameWindow(this);
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
				exception.printStackTrace();
				this.clientUI.onDesync(new DesynchronizationException(exception));
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
		
		this.clientUI.onTurn();
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
		return null;
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

	// ============================================================================================
	// === WAITING WINDOW
	// ============================================================================================

	private final class ConnectionFrame extends JFrame implements Runnable
	{
		private static final long serialVersionUID = 1L;
		private final Label statusLabel;

		public ConnectionFrame(final String playerName)
		{
			super("Connexion :: Client :: " + playerName);

			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			setLayout(new MigLayout("", "[240, center]", "[80]"));

			this.statusLabel = new Label("Connecting to server...");
			add(this.statusLabel, "span");

			pack();

			new Thread(this).start();
		}

		@Override
		public void run()
		{
			try
			{
				DefaultGuiClient.this.server.connect(DefaultGuiClient.this);
				onSuccess();
			}
			catch (final ServerConnectionException exception)
			{
				onFailure(exception);
			}
			catch (final RemoteServerException exception)
			{
				onFailure(exception);
			}
		}

		private void onSuccess()
		{
			this.statusLabel.setText("Waiting for game to start...");
		}

		private void onFailure(final ServerConnectionException exception)
		{
			exception.printStackTrace();
			this.statusLabel.setText("Cannot connect to the server.");
		}

		private void onFailure(final RemoteServerException exception)
		{
			exception.printStackTrace();
			this.statusLabel.setText("Server returned an error.");
		}
	}
}
