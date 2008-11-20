package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game
{
	private final ITileSequence sequence;
	private final List<Player> players = new ArrayList<Player>();
	private final Map<Player, List<Meeple>> meeples = new HashMap<Player, List<Meeple>>();
	private int currentPlayerIndex = 0;
	private final Board board;
	private final String name;
	private boolean finished = false;

	public Game(final String name, final ITileSequence sequence, final List<Player> players)
	{
		this.name = name;
		this.players.addAll(players);

		for (Player player : getPlayers())
		{
			meeples.put(player, new ArrayList<Meeple>());
			
			for (int i = 0; i < 7; i++) 
			{
				meeples.get(player).add(new Meeple());
			}
		}

		this.sequence = sequence;
		this.board = new Board(new Geometry());
	}
	
	public void endTurn()
	{
		if (this.sequence.hasMoreTiles())
		{
			this.sequence.nextTile();
		}
		else
		{
			this.finished = true;
			this.currentPlayerIndex = -1;
		}
	}
	
	public void doTurn(final Turn turn) throws InvalidTileLocationException, FeatureTakenException
	{
		getBoard().placeTile(getCurrentTile(), turn.getLocation(), turn.getDirection());

		if (turn.getMeeple() != null)
		{
			getBoard().placeMeeple(turn.getMeeple(), turn.getSection());
		}

		if (!turn.isNonPlayer())
		{
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
		}

		endTurn();
	}

	public Tile getCurrentTile()
	{
		return isFinished() ? null : this.sequence.getCurrentTile();
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
	
	public ITileSequence getSequence()
	{
		return this.sequence;
	}

	public Board getBoard()
	{
		return this.board;
	}

	public List<Player> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}
	
	public List<Meeple> getPlayerMeeples(Player player)
	{
		return Collections.unmodifiableList(this.meeples.get(player));
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
