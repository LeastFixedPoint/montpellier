package info.reflectionsofmind.connexion.core.board.geometry;

public interface IDirection
{
	IGeometry getGeometry();
	int getIndex();
	IDirection next();
	IDirection add(IDirection direction);
}
