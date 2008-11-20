package info.reflectionsofmind.connexion.core.game.sequence;

import info.reflectionsofmind.connexion.core.tile.Tile;

public interface ITileSequence
{
	Tile getCurrentTile();
	void nextTile();
	boolean hasMoreTiles();
	Integer getTotalNumberOfTiles();
}
