package info.reflectionsofmind.connexion.tilelist;

import info.reflectionsofmind.connexion.core.tile.Tile;

public final class TileSourceUtil
{
	private TileSourceUtil()
	{
		throw new UnsupportedOperationException();
	}

	public static TileData getTileData(final ITileSource tileSource, final Tile tile)
	{
		for (final TileData tileData : tileSource.getTiles())
		{
			if (tileData.getTile() == tile) return tileData;
		}

		return null;
	}
}
