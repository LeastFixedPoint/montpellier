package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;

public class Player implements IPlayer
{
	private int score = 0;

	public void addScore(int completedScore)
	{
		this.score += completedScore;
	}
}
