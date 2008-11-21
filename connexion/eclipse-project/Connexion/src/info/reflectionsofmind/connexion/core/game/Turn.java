package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;

public class Turn
{
	private final boolean advancePlayer;

	private IDirection direction;
	private ILocation location;
	private Meeple.Type meepleType;
	private Section section;

	public Turn()
	{
		this(true);
	}

	public Turn(final boolean advancePlayer)
	{
		this.advancePlayer = advancePlayer;
	}
	
	public void addTilePlacement(final ILocation location, final IDirection direction)
	{
		this.location = location;
		this.direction = direction;
	}
	
	public void addMeeplePlacement(final Meeple.Type meepleType, final Section section)
	{
		this.meepleType = meepleType;
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
	
	public Meeple.Type getMeepleType()
	{
		return this.meepleType;
	}

	public Section getSection()
	{
		return this.section;
	}
	
	public boolean isAdvancePlayer()
	{
		return this.advancePlayer;
	}
}
