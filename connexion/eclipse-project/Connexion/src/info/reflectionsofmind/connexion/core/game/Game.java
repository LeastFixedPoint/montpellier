package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.InvalidLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.tilelist.ITileGenerator;
import info.reflectionsofmind.connexion.tilelist.DoublePoint;
import info.reflectionsofmind.connexion.tilelist.TileData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game
{
	private final ITileGenerator generator;
	private final List<Player> players = new ArrayList<Player>();
	private final int currentPlayerIndex = 0;
	private final Board board;

	public Game(final ITileGenerator generator, final List<Player> players)
	{
		this.players.addAll(players);
		this.generator = generator;
		this.board = new Board(new Geometry(), generator.currentTile().getTile());
		generator.nextTile();
	}
	
	public Tile getCurrentTile()
	{
		return generator.currentTile().getTile();
	}
	
	public TileData getTileData(final Tile tile)
	{
		return generator.getTileData(tile);
	}

	public void doTurn(final Turn turn) throws NotYourTurnException, InvalidLocationException
	{
		if (turn.getPlayer() != getCurrentPlayer())
		{
			throw new NotYourTurnException(getCurrentPlayer(), turn.getPlayer());
		}

		getBoard().placeTile(turn.getTile(), turn.getLocation(), turn.getDirection());

		if (generator.hasMoreTiles())
		{
			generator.nextTile();
		}
	}

	public Player getCurrentPlayer()
	{
		return getPlayers().get(currentPlayerIndex);
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
