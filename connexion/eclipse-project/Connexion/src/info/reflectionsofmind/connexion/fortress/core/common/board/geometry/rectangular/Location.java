package info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular;

import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;

public class Location implements ILocation
{
	private final int x;
	private final int y;
	private final RectangularGeometry geometry;

	public Location(final RectangularGeometry geometry, final int x, final int y)
	{
		this.geometry = geometry;
		this.x = x;
		this.y = y;
	}

	@Override
	public Location shift(final IDirection direction)
	{
		return getGeometry().newLocation(//
				getX() + getGeometry().cast(direction).getDx(),//
				getY() + getGeometry().cast(direction).getDy());
	}

	@Override
	public Direction getOpposingDirection(IDirection direction)
	{
		return getGeometry().cast(direction).inverse();
	}
	
	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}
	
	@Override
	public RectangularGeometry getGeometry()
	{
		return this.geometry;
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof Location && // 
				((Location) obj).x == this.x && ((Location) obj).y == this.y;
	}
	
	@Override
	public String toString()
	{
		return "Location: " + getX() + ":" + getY() + "";
	}
}
