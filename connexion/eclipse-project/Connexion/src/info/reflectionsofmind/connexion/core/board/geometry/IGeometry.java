package info.reflectionsofmind.connexion.core.board.geometry;

import java.util.List;

public interface IGeometry
{
	ILocation getInitialLocation();
	int getNumberOfDirections();
	IDirection getNthDirection(int n);
	List<IDirection> getDirections();
}
