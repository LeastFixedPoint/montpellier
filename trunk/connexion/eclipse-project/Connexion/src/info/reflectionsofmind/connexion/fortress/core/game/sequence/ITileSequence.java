package info.reflectionsofmind.connexion.fortress.core.game.sequence;

import info.reflectionsofmind.connexion.fortress.core.tile.Tile;

public interface ITileSequence
{
	Tile getCurrentTile();
	void nextTile();
	boolean hasMoreTiles();
	Integer getTotalNumberOfTiles();
}
