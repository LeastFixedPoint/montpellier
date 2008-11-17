package info.reflectionsofmind.connexion.tilelist;

import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

public class DefaultTileGenerator implements ITileGenerator
{
	private final List<TileData> tiles = new ArrayList<TileData>();
	private final List<TileData> sequence = new ArrayList<TileData>();
	private int currentTileIndex = 0;

	public DefaultTileGenerator(final URL url) throws IOException, TileCodeFormatException
	{
		final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

		while (reader.ready())
		{
			final TileData tileData = readTileData(reader);
			this.tiles.add(tileData);
			this.sequence.add(tileData);
		}
		
		Collections.shuffle(this.sequence);

		reader.close();
	}

	private TileData readTileData(final BufferedReader reader) throws IOException, TileCodeFormatException
	{
		final String imageUrl = reader.readLine();
		final String tileCode = reader.readLine();
		final String[] points = reader.readLine().split(",");

		final Tile tile = new Tile(tileCode);
		final BufferedImage image = ImageIO.read(new URL(imageUrl));
		final TileData tileData = new TileData(tile, image);

		final Iterator<Section> sections = tile.getSections().iterator();
		for (final String point : points)
		{
			if (sections.hasNext())
			{
				tileData.addSectionPoint(sections.next(), DoublePoint.fromString(point));
			}
			else
			{
				throw new RuntimeException("There are more points than sections in tile [" + tileCode + "].");
			}
		}

		return tileData;
	}

	@Override
	public boolean hasMoreTiles()
	{
		return this.currentTileIndex < this.sequence.size();
	}

	@Override
	public TileData currentTile()
	{
		return this.sequence.get(this.currentTileIndex);
	}

	@Override
	public void nextTile()
	{
		this.currentTileIndex++;
	}

	@Override
	public TileData getTileData(final Tile tile)
	{
		for (TileData tileData : this.tiles)
		{
			if (tileData.getTile() == tile)
			{
				return tileData;
			}
		}
		
		return null;
	}
}
