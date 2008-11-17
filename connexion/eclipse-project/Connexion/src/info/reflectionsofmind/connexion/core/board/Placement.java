package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.util.Loop;

import java.util.ArrayList;
import java.util.List;

public class Placement
{
	private final Board board;
	private final Tile tile;
	private final ILocation location;
	private final IDirection direction;

	public Placement(final Board board, final Tile tile, final ILocation location, final IDirection direction)
	{
		this.board = board;
		this.tile = tile;
		this.location = location;
		
		if (direction == null)
		{
			throw new RuntimeException("OMGLOL");
		}
		
		this.direction = direction;
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

	public Board getBoard()
	{
		return this.board;
	}

	public Loop<Side> getSides()
	{
		final List<Side> sides = new ArrayList<Side>();

		final IDirection direction = getDirection();
		
		if (direction == null)
		{
			throw new RuntimeException("Ooops");
		}
		
		final int d = direction.getIndex();
		final int n = getBoard().getGeometry().getNumberOfDirections();

		for (int i = 0; i < n; i++)
		{
			sides.add(getTile().getSides().get(i + d));
		}

		return new Loop<Side>(sides);
	}

	public Side getSide(final IDirection direction)
	{
		final int index = direction.getIndex() + getDirection().getIndex();
		return this.tile.getSides().get(index);
	}

	public IDirection getDirectionOfSide(final Side side)
	{
		return getBoard().getGeometry().getDirections().get(getSides().indexOf(side));
	}

	public Side getSideForDirection(final IDirection direction)
	{
		return getSides().get(direction.getIndex());
	}
}
