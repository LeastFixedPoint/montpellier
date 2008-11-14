package info.reflectionsofmind.connexion.core.board.geometry.rectangular;

import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.IOffset;

public class Location implements ILocation
{
	private final int x;
	private final int y;

	public Location(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public Location offset(final IOffset offset)
	{
		if (!(offset instanceof Offset)) throw new RuntimeException("Incompartible offset [" + offset + "] for location [" + this + "].");
		return new Location(getX() + ((Offset) offset).getDx(), getY() + ((Offset) offset).getDy());
	}

	@Override
	public IOffset getAdjacentOffset(final IOffset offset)
	{
		if (!(offset instanceof Offset)) throw new RuntimeException("Incompartible offset [" + offset + "] for location [" + this + "].");
		return ((Offset) offset).inverse();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof Location && // 
				((Location) obj).x == this.x && ((Location) obj).y == this.y;
	}
}
