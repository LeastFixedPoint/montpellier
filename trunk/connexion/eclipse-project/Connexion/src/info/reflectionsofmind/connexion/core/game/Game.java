package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game
{
	private final ITileSequence sequence;
	private final List<Player> players = new ArrayList<Player>();
	private int currentPlayerIndex = 0;
	private final Board board;
	private Tile currentTile;
	private final String name;
	private boolean finished = false;

	public Game(final String name, final ITileSequence sequence, final List<Player> players)
	{
		this.name = name;
		this.players.addAll(players);
		this.sequence = sequence;
		this.board = new Board(new Geometry());
		this.currentTile = sequence.nextTile();
	}

	public void doTurn(final Turn turn) throws InvalidTileLocationException
	{
		getBoard().placeTile(getCurrentTile(), turn.getLocation(), turn.getDirection());

		if (!turn.isNonPlayer())
		{
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
		}

		if (this.sequence.hasMoreTiles())
		{
			this.currentTile = this.sequence.nextTile();
		}
		else
		{
			this.finished = true;
			this.currentTile = null;
			this.currentPlayerIndex = -1;
		}
	}

	public Tile getCurrentTile()
	{
		return this.currentTile;
	}

	public Player getCurrentPlayer()
	{
		if (this.currentPlayerIndex != -1)
		{
			return getPlayers().get(this.currentPlayerIndex);
		}
		else
		{
			return null;
		}
	}

	public Board getBoard()
	{
		return this.board;
	}

	public List<Player> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}

	public String getName()
	{
		return this.name;
	}
	
	public boolean isFinished()
	{
		return this.finished;
	}
}
