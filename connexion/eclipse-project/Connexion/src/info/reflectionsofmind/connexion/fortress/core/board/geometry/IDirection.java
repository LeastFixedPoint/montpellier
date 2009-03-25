package info.reflectionsofmind.connexion.fortress.core.board.geometry;

public interface IDirection
{
	IGeometry getGeometry();
	int getIndex();
	IDirection next();
	IDirection prev();
}
