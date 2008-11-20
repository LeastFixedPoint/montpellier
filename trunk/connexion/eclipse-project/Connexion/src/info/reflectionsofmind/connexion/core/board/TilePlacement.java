package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.util.Loop;

public class TilePlacement
{
	private final Board board;
	private final OrientedTile orientedTile;
	private final ILocation location;

	public TilePlacement(final Board board, final OrientedTile orientedTile, final ILocation location)
	{
		this.board = board;
		this.orientedTile = orientedTile;
		this.location = location;
	}

	public OrientedTile getOrientedTile()
	{
		return this.orientedTile;
	}

	public Tile getTile()
	{
		return getOrientedTile().getTile();
	}

	public IDirection getDirection()
	{
		return getOrientedTile().getDirection();
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public Board getBoard()
	{
		return this.board;
	}

	public Loop<Side> getSides()
	{
		return getOrientedTile().getSides();
	}

	public Side getSide(final IDirection direction)
	{
		final int index = direction.getIndex() + getDirection().getIndex();
		return getTile().getSides().get(index);
	}

	public IDirection getDirectionOfSide(final Side side)
	{
		return getBoard().getGeometry().getDirections().get(getSides().indexOf(side));
	}

	public Side getSideForDirection(final IDirection direction)
	{
		return getSides().get(direction.getIndex());
	}

	@Override
	public String toString()
	{
		return "Placement@" + hashCode() + ": [" + getTile() + "], [" + getLocation() + "], [" + getDirection() + "]";
	}
}
