package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;

public class Turn
{
	private final IDirection direction;
	private final ILocation location;
	private final Meeple meeple;
	private final Section section;
	private final boolean nonPlayer;

	public Turn( //
			final ILocation location, //
			final IDirection direction, // 
			final Meeple meeple, //
			final Section section, //
			final boolean nonPlayer)
	{
		this.direction = direction;
		this.location = location;
		this.meeple = meeple;
		this.section = section;
		this.nonPlayer = nonPlayer;
	}
	

	public Turn( //
			final ILocation location, //
			final IDirection direction, // 
			final Meeple meeple, //
			final Section section)
	{
		this(location, direction, meeple, section, false);
	}

	
	public ILocation getLocation()
	{
		return this.location;
	}

	public IDirection getDirection()
	{
		return this.direction;
	}
	
	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}
	
	public boolean isNonPlayer()
	{
		return this.nonPlayer;
	}
}
