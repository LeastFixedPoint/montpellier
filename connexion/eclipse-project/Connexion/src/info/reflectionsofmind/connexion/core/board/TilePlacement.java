package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.util.Loop;

public class TilePlacement
{
	private final Board board;
	private final Tile tile;
	private final IDirection direction;
	private final ILocation location;

	public TilePlacement(final Board board, final Tile tile, final IDirection direction, final ILocation location)
	{
		this.board = board;
		this.direction = direction;
		this.location = location;
		this.tile = tile;
	}

	public Tile getTile()
	{
		return this.tile;
	}

	public IDirection getDirection()
	{
		return this.direction;
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
		return getTile().getSides().roll(getDirection().getIndex());
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
