package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.tile.Tile;

import java.awt.image.BufferedImage;

public interface ITileGenerator
{
	boolean hasMoreTiles();
	Tile nextTile();
	BufferedImage getTileImage(Tile tile);
}
