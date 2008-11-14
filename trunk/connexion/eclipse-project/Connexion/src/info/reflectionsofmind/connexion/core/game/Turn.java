package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;

public class Turn
{
	private final Player player;
	private final Tile tile;
	private final ILocation location;
	private final IDirection direction;
	private final Meeple meeple;
	private final Section section;

	public Turn(final Player player, final Tile tile, final ILocation location, final IDirection direction, final Meeple meeple, final Section section)
	{
		this.player = player;
		this.tile = tile;
		this.location = location;
		this.direction = direction;
		this.meeple = meeple;
		this.section = section;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Tile getTile()
	{
		return this.tile;
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
}
