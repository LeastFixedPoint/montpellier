package info.reflectionsofmind.connexion.core.board.geometry.rectangular;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;

public class Geometry implements IGeometry
{
	@Override
	public ILocation getInitialLocation()
	{
		return new Location(0, 0);
	}
}
