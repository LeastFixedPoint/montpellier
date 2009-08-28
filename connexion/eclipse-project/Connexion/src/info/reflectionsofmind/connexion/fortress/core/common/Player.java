package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayer
{
	private int score = 0;
	private final List<Meeple> meeples = new ArrayList<Meeple>();

	public void addScore(final int completedScore)
	{
		this.score += completedScore;
	}

	public List<Meeple> getMeeples()
	{
		return this.meeples;
	}
	
	public int getScore()
	{
		return this.score;
	}

	public String getName()
	{
		return "Player";
	}
}
