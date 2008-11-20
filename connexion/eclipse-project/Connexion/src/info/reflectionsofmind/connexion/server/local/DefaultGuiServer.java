package info.reflectionsofmind.connexion.server.local;

import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
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

	@Override
	public void register(final IRemoteClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.add(client);
		
		this.serverUI.updateInterface();
	}

	@Override
	public void startGame(final String name)
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
				tiles.add(tileData.getTile());
			}

			final ITileSequence sequence = new RandomTileSequence(tiles);

			this.game = new Game(name, sequence, players);
			this.gameStarted = true;
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}

		for (final IRemoteClient client : this.clients)
		{
			try
			{
				client.sendStart(new StartEvent( //
						getGame().getName(), //
						getGame().getPlayers(), //
						this.players.get(client), //
						getGame().getCurrentTile()));
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}
		
		this.serverUI.updateInterface();
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
		final Tile placedTile = getGame().getCurrentTile();
		
		try
		{
			this.game.doTurn(new Turn( //
					event.getDirection(), //
					event.getLocation(), //
					event.getMeeple(), //
					event.getSection()));

			for (final IRemoteClient client : getClients())
			{
				if (client != event.getClient())
				{
					try
					{
						client.sendTurn(new ServerTurnEvent( //
								this.players.get(event.getClient()), //
								new OrientedTile(placedTile, event.getDirection()), //
								event.getLocation(), //
								event.getMeeple(), //
								event.getSection(), //
								getGame().getCurrentPlayer(), //
								getGame().getCurrentTile() //
								));
					}
					catch (final ClientConnectionException exception)
					{
						exception.printStackTrace();
					}
				}
			}
		}
		catch (final NotYourTurnException exception)
		{
			exception.printStackTrace();
		}
		catch (final InvalidTileLocationException exception)
		{
			exception.printStackTrace();
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
