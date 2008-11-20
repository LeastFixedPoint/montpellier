package info.reflectionsofmind.connexion.client.local;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.gui.ClientUI;
import info.reflectionsofmind.connexion.client.remote.IRemoteServer;
import info.reflectionsofmind.connexion.client.remote.RemoteServerException;
import info.reflectionsofmind.connexion.client.remote.ServerConnectionException;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

import java.awt.Label;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class DefaultGuiClient implements IClient
{
	// All of this we have always
	private final IRemoteServer server;
	private final String name;
	private final ConnectionFrame conectionFrame;
	private final ITileSource tileSource;

	// All of this we have after onStart
	private RemoteTileSequence sequence;
	private ClientUI clientUI;
	private Player player;
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
	public void onStart(final StartEvent event)
	{
		this.player = event.getClientPlayer();
		this.sequence = new RemoteTileSequence();
		this.sequence.push(event.getInitialTile());
		this.sequence.push(event.getCurrentTile());
		this.game = new Game(event.getGameName(), this.sequence, event.getPlayers());

		try
		{
			this.game.doTurn(new Turn(//
					this.game.getBoard().getGeometry().getInitialLocation(), //
					this.game.getBoard().getGeometry().getDirections().get(0),
					(Meeple)null, (Section)null, true));
		}
		catch (InvalidTileLocationException exception)
		{
			throw new RuntimeException("Invalid initial location.");
		}
		
		this.conectionFrame.dispose();

		this.clientUI = new ClientUI(this);
	}

	@Override
	public void onTurn(final ServerTurnEvent event)
	{
		if (this.sequence.currentTile() != null && this.sequence.currentTile() != event.getOrientedTile().getTile())
		{
			this.clientUI.onDesync(new DesynhronizationException(new RuntimeException("Tile sequence desync.")));
		}

		if (event.getCurrentTile() != null)
		{
			this.sequence.push(event.getCurrentTile());
		}
		else
		{
			this.sequence.setNoMoreTiles();
		}
		
		try
		{
			this.game.doTurn(new Turn( //
					event.getLocation(), //
					event.getOrientedTile().getDirection(), // 
					event.getMeeple(), //
					event.getSection(), //
					false));
		}
		catch (InvalidTileLocationException exception)
		{
			this.clientUI.onDesync(new DesynhronizationException(exception));
		}
		
		if (event.getCurrentPlayer() != null && event.getCurrentPlayer() != getGame().getCurrentPlayer())
		{
			this.clientUI.onDesync(new DesynhronizationException(new RuntimeException("Player sequence desync.")));
		}

		this.clientUI.onTurn(event);
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

	// ============================================================================================
	// === TILE SEQUENCE PROXY
	// ============================================================================================

	private final class RemoteTileSequence implements ITileSequence
	{
		private final Deque<Tile> queue = new ArrayDeque<Tile>(1);
		private boolean hasMoreTiles = true;

		public void push(final Tile tile)
		{
			this.queue.addFirst(tile);
		}

		public void setNoMoreTiles()
		{
			this.hasMoreTiles = false;
		}

		public Tile currentTile()
		{
			return this.queue.peekLast();
		}

		@Override
		public Tile nextTile()
		{
			return this.queue.removeLast();
		}

		@Override
		public boolean hasMoreTiles()
		{
			return this.hasMoreTiles;
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
