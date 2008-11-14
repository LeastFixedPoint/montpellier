package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.core.util.Loop;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game
{
	private final ITileGenerator generator;
	private final List<Player> players = new ArrayList<Player>();
	private final Loop<Player> currentPlayer = new Loop<Player>(this.players);
	private final Board board;
	private Tile currentTile;

	public Game(final ITileGenerator generator, final List<Player> players)
	{
		this.players.addAll(players);
		this.generator = generator;
		this.board = new Board(new Geometry(), generator.nextTile());
		this.currentTile = generator.nextTile();
	}
	
	public Tile getNextTile()
	{
		try
		{
			return new Tile("c1c2f1f2f3f4r1r2|f1r1f2c2f3r2f4,f2c1f3,c1r1,c1r1,r2c1c2,c1r2,f1f2c1,f3f4c1|c1,f1r1f2,c2,f3r2f4");
		}
		catch (final TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
		
		// TODO Tile sequence
	}

	public URL getTileImageURL(final Tile tile)
	{
		return getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/resources/tile.png");
	}

	public void doTurn(final Turn turn) throws NotYourTurnException
	{
		if (turn.getPlayer() != getCurrentPlayer())
		{
			throw new NotYourTurnException(getCurrentPlayer(), turn.getPlayer());
		}

		// TODO Turn processing

		if (generator.hasMoreTiles())
		{
			this.currentTile = generator.nextTile();
		}
	}

	public Player getCurrentPlayer()
	{
		return this.currentPlayer.current();
	}

	public Board getBoard()
	{
		return this.board;
	}

	public List<Player> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}
}
