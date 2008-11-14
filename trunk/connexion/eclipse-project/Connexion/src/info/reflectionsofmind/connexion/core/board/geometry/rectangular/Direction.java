package info.reflectionsofmind.connexion.core.board.geometry.rectangular;

import info.reflectionsofmind.connexion.core.board.geometry.GeometryException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;

public class Direction implements IDirection
{
	private final static int[][] COORDS = new int[][] { { 0, +1 }, { +1, 0 }, { 0, -1 }, { -1, 0 } };
	private final Geometry geometry;
	private final int index;

	public Direction(final Geometry geometry, final int index)
	{
		if (index >= geometry.getNumberOfDirections())
		{
			throw new GeometryException("Impossible direction index [" + index + "].");
		}
		
		this.geometry = geometry;
		this.index = index;
	}

	// ============================================================================================
	// === IMPLEMENTATION
	// ============================================================================================

	@Override
	public Geometry getGeometry()
	{
		return this.geometry;
	}

	@Override
	public int getIndex()
	{
		return this.index;
	}

	@Override
	public Direction next()
	{
		return getGeometry().newDirection(getIndex() + 1);
	}

	@Override
	public IDirection add(IDirection direction)
	{
		return getGeometry().getNthDirection(getIndex() + direction.getIndex());
	}

	// ============================================================================================
	// === UTILITY
	// ============================================================================================

	public Direction inverse()
	{
		return getGeometry().newDirection(getIndex() + getGeometry().getNumberOfDirections() / 2);
	}

	public int getDx()
	{
		return COORDS[index][0];
	}

	public int getDy()
	{
		return COORDS[index][1];
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof Direction && // 
				((Direction) obj).getDx() == getDx() && ((Direction) obj).getDy() == getDy();
	}
	
	@Override
	public String toString()
	{
		return "Direction: " + getDx() + ":" + getDy() + "";
	}
}
