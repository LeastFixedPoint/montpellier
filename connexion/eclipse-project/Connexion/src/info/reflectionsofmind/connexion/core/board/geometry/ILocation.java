package info.reflectionsofmind.connexion.core.board.geometry;

public interface ILocation
{
	/** Returns location adjacent to current by given offset. */
	ILocation offset(IOffset offset);

	/** Returns offset of the adjacent location (by given offset) that corresponds to given offset. */
	IOffset getAdjacentOffset(IOffset offset);
}
