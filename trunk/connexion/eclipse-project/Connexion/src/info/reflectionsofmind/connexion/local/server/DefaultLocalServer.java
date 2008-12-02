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
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;
import info.reflectionsofmind.connexion.local.server.gui.HostGameWindow;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.connector.IClientConnector;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

public class DefaultLocalServer implements IServer, IRemoteClient.IListener
{
	private final List<ISlot> slots = new ArrayList<ISlot>();

	private Game game;
	private ITileSource tileSource;

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	public void addSlot(ISlot slot)
	{
		if (this.game != null) throw new RuntimeException("Game already started.");

		slot.init(this);
	}

	private void sendStartEvents(final Tile initialTile)
	{
		for (final ISlot slot : this.slots)
		{
			if (slot.getState() == ISlot.State.CONNECTED)
			{
				try
				{
					slot.getClient().sendStart(getGame());
				}
				catch (final ClientConnectionException exception)
				{
					exception.printStackTrace();
				}
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

			final List<Player> players = new ArrayList<Player>();
			for (ISlot slot : this.slots)
			{
				players.add(slot.getPlayer());
			}

			this.game = new Game(name, new RandomTileSequence(tiles), players);
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

	// ============================================================================================
	// === HANDLERS
	// ============================================================================================

	@Override
	public synchronized void onConnectionRequest(ISlot senderSlot, ClientToServer_ClientConnectionRequestEvent event)
	{
		final Player player = new Player(event.getPlayerName());
		senderSlot.accept(player);
		senderSlot.getClient().acceptConnection(ServerUtil.getPlayers(this));
		
		for (ISlot slot : ServerUtil.getConnectedSlots(this))
		{
			if (slot != senderSlot)
			{
				slot.getClient().sendPlayerConnected(player);
			}
		}
	}

	@Override
	public void onMessage(IRemoteClient sender, ClientToServer_MessageEvent event)
	{
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

		for (final IRemoteClient client : ServerUtil.getClients(this))
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
	}

	@Override
	public synchronized void onDisconnect(final IRemoteClient sender, ClientToServer_ClientDisconnectedEvent event)
	{
		throw new UnsupportedOperationException("Disconnects not supported yet.");
	}
}
