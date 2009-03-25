package info.reflectionsofmind.connexion.fortress.core.board.geometry;

public class IncompartibleGeometryException extends GeometryException
{
	private static final long serialVersionUID = 1L;

	public IncompartibleGeometryException(final Object g1, final Object g2)
	{
		super("Incompartible geometry objects [" + g1 + "] and [" + g2 + "].");
	}
}
