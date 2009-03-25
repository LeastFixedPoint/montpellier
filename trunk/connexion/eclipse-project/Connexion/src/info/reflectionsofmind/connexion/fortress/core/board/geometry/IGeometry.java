package info.reflectionsofmind.connexion.fortress.core.board.geometry;

import info.reflectionsofmind.connexion.fortress.core.util.Loop;

public interface IGeometry
{
	ILocation getInitialLocation();
	int getNumberOfDirections();
	Loop<IDirection> getDirections();
}
