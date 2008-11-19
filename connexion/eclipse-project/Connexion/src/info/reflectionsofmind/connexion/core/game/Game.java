package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
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

	public Game(final String name, final ITileSequence sequence, final List<Player> players)
	{
		this.name = name;
		this.players.addAll(players);
		this.sequence = sequence;
		this.board = new Board(new Geometry(), sequence.nextTile());
		this.currentTile = sequence.nextTile();
	}

	public void doTurn(final Turn turn) throws NotYourTurnException, InvalidTileLocationException
	{
		if (turn.getPlayer() != getCurrentPlayer())
		{
			throw new NotYourTurnException(getCurrentPlayer(), turn.getPlayer());
		}

		getBoard().placeTile(turn.getTile(), turn.getLocation(), turn.getDirection());

		if (this.sequence.hasMoreTiles())
		{
			this.currentTile = this.sequence.nextTile();
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
		}
		else
		{
			this.currentTile = null;
			this.currentPlayerIndex = -1;
		}
	}

	public void removePlayer(final Player player)
	{
		final int index = this.players.indexOf(player);

		if (this.currentPlayerIndex > index)
		{
			this.currentPlayerIndex--;
		}

		this.currentPlayerIndex = this.currentPlayerIndex % this.players.size();

		this.players.remove(index);
	}

	public Tile getCurrentTile()
	{
		return this.currentTile;
	}

	public Player getCurrentPlayer()
	{
		return getPlayers().get(this.currentPlayerIndex);
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
}
