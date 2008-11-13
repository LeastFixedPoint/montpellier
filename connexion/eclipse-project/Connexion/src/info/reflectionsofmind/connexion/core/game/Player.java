package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.util.Multi;

public class Player
{
	private final String name;
	private final Multi<Player, Meeple> meeples = new Multi<Player, Meeple>(this);

	public Player(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public Multi<Player, Meeple> getMeeples()
	{
		return this.meeples;
	}
}
