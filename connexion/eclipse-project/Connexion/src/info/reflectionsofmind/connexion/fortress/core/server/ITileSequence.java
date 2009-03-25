package info.reflectionsofmind.connexion.fortress.core.server;

import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;

public interface ITileSequence
{
	/** @returns next tile or <code>null</code> if tiles are over. */
	Tile getNextTile();
}
