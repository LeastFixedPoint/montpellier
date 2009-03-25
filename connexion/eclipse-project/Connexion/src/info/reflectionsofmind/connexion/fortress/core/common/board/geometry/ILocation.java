package info.reflectionsofmind.connexion.fortress.core.common.board.geometry;


public interface ILocation
{
	IGeometry getGeometry();
	IDirection getOpposingDirection(IDirection direction);
	ILocation shift(IDirection direction);
}
