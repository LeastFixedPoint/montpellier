package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
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

	public List<Side> getSides()
	{
		final List<Side> sides = new ArrayList<Side>();

		final int d = getDirection().getIndex();
		final int n = getBoard().getGeometry().getNumberOfDirections();

		for (int i = 0; i < n; i++)
		{
			sides.add(getTile().getSides().get((i + d) % n));
		}

		return Collections.unmodifiableList(sides);
	}

	public Side getSide(final IDirection direction)
	{
		final int index = direction.getIndex() + getDirection().getIndex();
		return this.tile.getSides().get(index);
	}

	public IDirection getDirectionOfSide(final Side side)
	{
		return getBoard().getGeometry().getNthDirection(getSides().indexOf(side));
	}

	public Side getSideForDirection(final IDirection direction)
	{
		return getSides().get(direction.getIndex());
	}
}
