package info.reflectionsofmind.connexion.fortress.core.game;

import info.reflectionsofmind.connexion.fortress.core.board.Meeple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player
{
	private final String name;
	private int score = 0;
	private final boolean disconnected = false;
	private final List<Meeple> meeples = new ArrayList<Meeple>();

	public Player(final String name)
	{
		this.name = name;
	}

	public void addMeeple(final Meeple meeple)
	{
		this.meeples.add(meeple);
	}

	public String getName()
	{
		return this.name;
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

	public List<Meeple> getMeeples()
	{
		return Collections.unmodifiableList(this.meeples);
	}
}
