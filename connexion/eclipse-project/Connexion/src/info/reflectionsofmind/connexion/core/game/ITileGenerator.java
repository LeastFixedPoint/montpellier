package info.reflectionsofmind.connexion.core.game;

import java.net.URL;

import info.reflectionsofmind.connexion.core.tile.Tile;

public interface ITileGenerator
{
	boolean hasMoreTiles();
	Tile nextTile();
	URL getImage(Tile tile);
}
