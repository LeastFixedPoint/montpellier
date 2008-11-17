package info.reflectionsofmind.connexion.tilelist;

public class DoublePoint
{
	private final double x;
	private final double y;

	public DoublePoint(final double x, final double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public static DoublePoint fromString(final String point)
	{
		final String[] cs = point.split(":");
		final double x = Double.parseDouble(cs[0]);
		final double y = Double.parseDouble(cs[1]);
		return new DoublePoint(x, y);
	}
}
