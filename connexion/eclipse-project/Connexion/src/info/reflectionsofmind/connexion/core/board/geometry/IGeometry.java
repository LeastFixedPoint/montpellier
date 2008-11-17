package info.reflectionsofmind.connexion.core.board.geometry;

import info.reflectionsofmind.connexion.core.util.Loop;

public interface IGeometry
{
	ILocation getInitialLocation();
	int getNumberOfDirections();
	Loop<IDirection> getDirections();
}
