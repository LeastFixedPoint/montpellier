package info.reflectionsofmind.connexion.core.board.geometry.rectangular;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.IncompartibleGeometryException;
import info.reflectionsofmind.connexion.core.util.Loop;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class Geometry implements IGeometry
{
	private final List<IDirection> DIRECTIONS = Arrays.<IDirection>asList( //
			new Direction(this, 0), new Direction(this, 1), new Direction(this, 2), new Direction(this, 3));

	// ============================================================================================
	// === IMPLEMENTATION
	// ============================================================================================

	@Override
	public int getNumberOfDirections()
	{
		return 4;
	}

	@Override
	public Location getInitialLocation()
	{
		return newLocation(0, 0);
	}

	@Override
	public Loop<IDirection> getDirections()
	{
		return new Loop<IDirection>(DIRECTIONS);
	}

	// ============================================================================================
	// === UTILITY
	// ============================================================================================

	public Direction getInitialDirection()
	{
		return newDirection(0);
	}

	public Direction cast(final IDirection offset)
	{
		if (!(offset instanceof Direction)) throw new IncompartibleGeometryException(this, offset);
		return (Direction) offset;
	}

	public Location cast(final ILocation location)
	{
		if (!(location instanceof Location)) throw new IncompartibleGeometryException(this, location);
		return (Location) location;
	}

	public Location newLocation(final int x, final int y)
	{
		return new Location(this, x, y);
	}

	public Direction newDirection(final int n)
	{
		return new Direction(this, n % getNumberOfDirections());
	}

	// ============================================================================================
	// === TESTING
	// ============================================================================================

	@Test
	public void $_test_nextLeft()
	{
		Direction direction = getInitialDirection();

		direction = (Direction) direction.next();
		Assert.assertEquals(+1, direction.getDx());
		Assert.assertEquals(0, direction.getDy());

		direction = (Direction) direction.next();
		Assert.assertEquals(0, direction.getDx());
		Assert.assertEquals(-1, direction.getDy());

		direction = (Direction) direction.next();
		Assert.assertEquals(-1, direction.getDx());
		Assert.assertEquals(0, direction.getDy());

		direction = (Direction) direction.next();
		Assert.assertEquals(0, direction.getDx());
		Assert.assertEquals(+1, direction.getDy());
	}
}
