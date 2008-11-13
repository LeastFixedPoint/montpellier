package info.reflectionsofmind.connexion.core.location;

import junit.framework.Assert;

import org.junit.Test;

public class Offset implements IOffset
{
	private final int dx;
	private final int dy;

	public Offset(final int dx, final int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	public Offset()
	{
		this(0, +1);
	}

	public int getDx()
	{
		return this.dx;
	}

	public int getDy()
	{
		return this.dy;
	}

	@Override
	public IOffset inverse()
	{
		return new Offset(-this.dx, -this.dy);
	}

	@Override
	public Offset nextRight()
	{
		return new Offset(this.dy, -this.dx);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof Offset && // 
				((Offset) obj).dx == this.dx && ((Offset) obj).dy == this.dy;
	}

	// ============================================================================================
	// === TESTING
	// ============================================================================================

	@Test
	public void $_test_nextLeft()
	{
		Offset offset = new Offset(0, +1);

		offset = offset.nextRight();
		Assert.assertEquals(+1, offset.dx);
		Assert.assertEquals(0, offset.dy);

		offset = offset.nextRight();
		Assert.assertEquals(0, offset.dx);
		Assert.assertEquals(-1, offset.dy);

		offset = offset.nextRight();
		Assert.assertEquals(-1, offset.dx);
		Assert.assertEquals(0, offset.dy);

		offset = offset.nextRight();
		Assert.assertEquals(0, offset.dx);
		Assert.assertEquals(+1, offset.dy);
	}
}
