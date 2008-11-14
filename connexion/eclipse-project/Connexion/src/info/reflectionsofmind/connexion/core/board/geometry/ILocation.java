package info.reflectionsofmind.connexion.core.board.geometry;


public interface ILocation
{
	IGeometry getGeometry();
	IDirection getOpposingDirection(IDirection direction);
	ILocation shift(IDirection direction);
}
