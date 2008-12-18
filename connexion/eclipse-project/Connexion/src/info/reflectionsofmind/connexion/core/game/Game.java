package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.BoardUtil;
import info.reflectionsofmind.connexion.core.board.Feature;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.MeeplePlacementException;
import info.reflectionsofmind.connexion.core.board.exception.TilePlacementException;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.exception.NoFreeMeeplesException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public class Game
{
	public final int MEEPLE_COUNT = 6;
	
	private final ITileSequence sequence;
	private final List<Player> players = new ArrayList<Player>();
	private int currentPlayerIndex = 0;
	private final Board board;
	private boolean finished = false;
	private final List<Turn> turns = new ArrayList<Turn>();

	public Game(final ITileSequence sequence, final List<Player> players)
	{
		this.players.addAll(players);

		for (final Player player : getPlayers())
		{
			for (int i = 0; i < 7; i++)
			{
				player.addMeeple(new Meeple(Meeple.Type.MEEPLE, player));
			}
		}

		this.sequence = sequence;
		this.board = new Board(new Geometry());
	}

	public void doTurn(final Turn turn) throws GameTurnException
	{
		try
		{
			getBoard().placeTile(getCurrentTile(), turn.getLocation(), turn.getDirection());
		}
		catch (TilePlacementException exception)
		{
			throw new GameTurnException(exception);
		}

		if (turn.getMeepleType() != null)
		{
			final Meeple freeMeeple = GameUtil.getFreeMeepleOfType(getBoard(), getCurrentPlayer(), turn.getMeepleType());
			
			if (freeMeeple == null)
			{
				throw new NoFreeMeeplesException();
			}
			
			try
			{
				getBoard().placeMeeple(freeMeeple, getCurrentTile().getSections().get(turn.getSectionIndex()));
			}
			catch (MeeplePlacementException exception)
			{
				throw new GameTurnException(exception);
			}
		}

		turns.add(turn);
		endTurn(turn.isAdvancePlayer());
	}

	public void endTurn(final boolean advancePlayer)
	{
		if (this.sequence.hasMoreTiles())
		{
			this.sequence.nextTile();

			if (advancePlayer)
			{
				this.currentPlayerIndex = (this.currentPlayerIndex + 1) % getPlayers().size();
			}
		}
		else
		{
			this.finished = true;
			this.currentPlayerIndex = -1;
		}

		scoreFeatures();
	}

	private void scoreFeatures()
	{
		final Set<Feature> scoredFeatures = new HashSet<Feature>();

		for (final Meeple meeple : getBoard().getMeeples())
		{
			final Feature feature = BoardUtil.getFeatureOf(getBoard(), getBoard().getMeepleSection(meeple));

			if (feature.isCompleted())
			{
				scoredFeatures.add(feature);
			}
		}

		for (final Feature feature : scoredFeatures)
		{
			scoreFeature(feature);
		}
	}

	private void scoreFeature(final Feature feature)
	{
		final Map<Player, List<Meeple>> meeplesByPlayer = GameUtil.getMeeplesByPlayer(this, feature);

		int max = 0;

		for (final Player player : meeplesByPlayer.keySet())
		{
			max = Math.max(max, meeplesByPlayer.get(player).size());
		}

		for (final Player player : meeplesByPlayer.keySet())
		{
			if (meeplesByPlayer.get(player).size() == max)
			{
				player.addScore(feature.getCompletedScore());
			}
		}

		for (final Meeple meeple : BoardUtil.getMeeplesOnFeature(getBoard(), feature))
		{
			getBoard().removeMeeple(meeple);
		}
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

	public boolean isFinished()
	{
		return this.finished;
	}
	
	public List<Turn> getTurns()
	{
		return ImmutableList.copyOf(this.turns);
	}
}
