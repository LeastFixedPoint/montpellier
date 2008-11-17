package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;

public class Turn
{
	private final Player player;
	private final OrientedTile orientedTile;
	private final ILocation location;
	private final Meeple meeple;
	private final Section section;

	public Turn(final Player player, final OrientedTile orientedTile, final ILocation location, final Meeple meeple, final Section section)
	{
		this.player = player;
		this.orientedTile = orientedTile;
		this.location = location;
		this.meeple = meeple;
		this.section = section;
	}

	public Player getPlayer()
	{
		return this.player;
	}
	
	public OrientedTile getOrientedTile()
	{
		return this.orientedTile;
	}
	
	public Tile getTile()
	{
		return getOrientedTile().getTile();
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public IDirection getDirection()
	{
		return getOrientedTile().getDirection();
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
