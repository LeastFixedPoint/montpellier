package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;
import info.reflectionsofmind.connexion.local.server.gui.ServerUI;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DefaultGuiServer implements IServer, IRemoteClient.IListener
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

	public void remove(final IRemoteClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.remove(client);

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
				client.sendStart(getGame());
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}
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

			final ITileSequence sequence = new RandomTileSequence(tiles);

			this.game = new Game(name, sequence, Util.map(this.players, this.clients));
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

		final Tile initialTile = this.game.getCurrentTile();

		try
		{
			placeInitialTile();
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}

		sendStartEvents(initialTile);

		this.serverUI.updateInterface();
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public List<IRemoteClient> getClients()
	{
		return Collections.unmodifiableList(this.clients);
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

	// ============================================================================================
	// === HANDLERS
	// ============================================================================================

	@Override
	public synchronized void onConnectionRequest(IRemoteClient sender, ClientToServer_ClientConnectionRequestEvent event)
	{
		this.clients.add(sender);
		this.players.put(sender, new Player(event.getPlayerName()));
		
		sender.acceptConnection(Util.map(this.players, this.clients));
		
		for (IRemoteClient client : this.clients)
		{
			if (client != sender)
			{
				client.sendPlayerConnected(players.get(clients.get(clients.size() - 1)));
			}
		}
	}
	
	@Override
	public synchronized void onTurn(final IRemoteClient sender, final ClientToServer_TurnEvent event)
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
				final Turn turn = (client == sender) ? null : event.getTurn();
				client.sendTurn(turn, getGame().getCurrentTile());
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}

		this.serverUI.updateInterface();
	}

	@Override
	public synchronized void onDisconnect(final IRemoteClient sender, ClientToServer_ClientDisconnectedEvent event)
	{
		throw new UnsupportedOperationException("Disconnects not supported yet.");
	}
}
