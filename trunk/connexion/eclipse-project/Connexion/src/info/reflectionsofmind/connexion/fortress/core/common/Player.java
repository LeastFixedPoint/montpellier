package info.reflectionsofmind.connexion.fortress.core.common;

import java.util.ArrayList;
import java.util.List;

import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;

public class Player implements IPlayer
{
	private int score = 0;
	private List<Meeple> meeples = new ArrayList<Meeple>();

	public void addScore(int completedScore)
	{
		this.score += completedScore;
	}

	public List<Meeple> getMeeples()
	{
		return this.meeples;
	}
}
