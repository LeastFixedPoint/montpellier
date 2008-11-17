package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LocalServer implements IServer
{
	private final List<IClient> clients = new ArrayList<IClient>();
	private final BiMap<IClient, Player> players = new HashBiMap<IClient, Player>();

	private boolean gameStarted = false;

	private Game game;
	private ITileSource tileSource;

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	@Override
	public void register(final IClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.add(client);
	}

	@Override
	public void startGame() throws ServerException
	{
		this.gameStarted = true;

		final List<Player> players = new ArrayList<Player>();

		for (final IClient client : this.clients)
		{
			final Player player = new Player(client.getName());
			this.players.put(client, player);
			players.add(player);
		}

		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));

			final  List<Tile> tiles = new ArrayList<Tile>();

			for (TileData tileData : this.tileSource.getTiles())
			{
				tiles.add(tileData.getTile());
			}

			final ITileSequence sequence = new RandomTileSequence(tiles);

			this.game = new Game(sequence, players);
		}
		catch (Exception exception)
		{
			throw new ServerException(exception);
		}

		for (final IClient client : this.clients)
		{
			client.onStart(this.players.get(client));
		}
	}

	@Override
	public Game getGame()
	{
		return this.gameStarted ?  this.game : null;
	}
	
	@Override
	public ITileSource getTileSource()
	{
		return this.gameStarted ?  this.tileSource : null;
	}

	@Override
	public void sendTurn(final Turn turn) throws InvalidTileLocationException, NotYourTurnException
	{
		this.game.doTurn(turn);

		for (IClient client : getClients())
		{
			client.onTurn(turn);
		}

		if (this.game.getCurrentTile() == null)
		{
			for (IClient client : getClients())
			{
				client.onEnd();
			}
		}
	}

	// ====================================================================================================
	// === UTILITY
	// ====================================================================================================
	
	public List<IClient> getClients()
	{
		return this.clients;
	}
}
