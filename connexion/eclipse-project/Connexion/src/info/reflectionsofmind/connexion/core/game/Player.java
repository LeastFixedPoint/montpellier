package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;

import java.util.ArrayList;
import java.util.List;

public class Player
{
	private final String name;
	private final List<Meeple> meeples = new ArrayList<Meeple>();
	private int score = 0;
	private boolean disconnected = false;

	public Player(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public List<Meeple> getMeeples()
	{
		return this.meeples;
	}

	public void addScore(final int bonus)
	{
		this.score += bonus;
	}

	public int getScore()
	{
		return this.score;
	}
	
	public boolean isDisconnected()
	{
		return this.disconnected;
	}
}
