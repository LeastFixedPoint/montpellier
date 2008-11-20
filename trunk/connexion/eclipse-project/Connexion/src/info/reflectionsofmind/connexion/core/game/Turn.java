package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;

public class Turn
{
	private final boolean nonPlayer;

	private IDirection direction;
	private ILocation location;
	private Meeple meeple;
	private Section section;

	public Turn()
	{
		this(false);
	}

	public Turn(final boolean nonPlayer)
	{
		this.nonPlayer = nonPlayer;
	}
	
	public void addTilePlacement(final ILocation location, final IDirection direction)
	{
		this.location = location;
		this.direction = direction;
	}
	
	public void addMeeplePlacement(final Meeple meeple, final Section section)
	{
		this.meeple = meeple;
		this.section = section;
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
