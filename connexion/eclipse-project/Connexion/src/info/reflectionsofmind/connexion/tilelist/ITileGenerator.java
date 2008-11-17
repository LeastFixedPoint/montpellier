package info.reflectionsofmind.connexion.tilelist;

import info.reflectionsofmind.connexion.core.tile.Tile;


public interface ITileGenerator
{
	boolean hasMoreTiles();
	void nextTile();
	TileData currentTile();
	TileData getTileData(Tile tile);
}
