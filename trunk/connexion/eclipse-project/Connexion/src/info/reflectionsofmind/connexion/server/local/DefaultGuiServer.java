package info.reflectionsofmind.connexion.server.local;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.exception.NotLastTileMeepleException;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.exception.NoFreeMeeplesException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.server.gui.ServerUI;
import info.reflectionsofmind.connexion.server.remote.ClientConnectionException;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.transport.ClientTurnEvent;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DefaultGuiServer implements IServer
{
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final BiMap<IRemoteClient, Player> players = new HashBiMap<IRemoteClient, Player>();
	private final ServerUI serverUI;

	private Game game;
	private boolean gameStarted = false;
	private ITileSource tileSource;

	public DefaultGuiServer(final ServerUI serverUI)
	{
		this.serverUI = serverUI;
	}

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	public void add(final IRemoteClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.add(client);

		this.serverUI.updateInterface();
	}

	@Override
	public void startGame(final String name)
	{
		createGame(name);

		final Tile initialTile = this.game.getCurrentTile();

		placeInitialTile();

		sendStartEvents(initialTile);

		this.serverUI.updateInterface();
	}

	private void sendStartEvents(final Tile initialTile)
	{
		for (final IRemoteClient client : this.clients)
		{
			final List<String> playerNames = new ArrayList<String>();

			for (Player player : getGame().getPlayers())
			{
				playerNames.add(player.getName());
			}

			try
			{
				client.sendStart(new StartEvent( //
						getGame().getName(), //
						playerNames, //
						getGame().getPlayers().indexOf(this.players.get(client)), //
						initialTile, getGame().getCurrentTile(), //
						getGame().getSequence().getTotalNumberOfTiles()));
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}
	}

	private void createGame(final String name)
	{
		final List<Player> players = new ArrayList<Player>();

		for (final IRemoteClient client : this.clients)
		{
			final Player player = new Player(client.getName());
			this.players.put(client, player);
			players.add(player);
		}

		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));

			final List<Tile> tiles = new ArrayList<Tile>();

			for (final TileData tileData : this.tileSource.getTiles())
			{
				tiles.add(new Tile(tileData.getCode()));
			}

			final ITileSequence sequence = new RandomTileSequence(tiles);

			this.game = new Game(name, sequence, players);
			this.gameStarted = true;
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

	private void placeInitialTile()
	{
		try
		{
			final Turn turn = new Turn(false);
			final IGeometry geometry = getGame().getBoard().getGeometry();
			turn.addTilePlacement(geometry.getInitialLocation(), geometry.getDirections().get(0));
			turn.addMeeplePlacement(null, null);

			getGame().doTurn(turn);
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	@Override
	public Game getGame()
	{
		return this.gameStarted ? this.game : null;
	}

	@Override
	public ITileSource getTileSource()
	{
		return this.gameStarted ? this.tileSource : null;
	}

	@Override
	public void onTurn(final ClientTurnEvent event)
	{
		try
		{
			this.game.doTurn(event.getTurn());
		}
		catch (final GameTurnException exception)
		{
			// TODO Server doesn't care about client's desync?
			exception.printStackTrace();
			return;
		}

		for (final IRemoteClient client : getClients())
		{
			try
			{
				final Turn turn = (client == event.getClient()) ? null : event.getTurn();
				client.sendTurn(new ServerTurnEvent(turn, getGame().getCurrentTile()));
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}

		this.serverUI.updateInterface();
	}

	// ====================================================================================================
	// === UTILITY
	// ====================================================================================================

	public List<IRemoteClient> getClients()
	{
		return this.clients;
	}
}
