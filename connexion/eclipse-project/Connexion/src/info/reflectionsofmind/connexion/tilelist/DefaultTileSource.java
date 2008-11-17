package info.reflectionsofmind.connexion.tilelist;

import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;

import java.awt.geom.Point2D;
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

public class DefaultTileSource implements ITileSource
{
	private final List<TileData> tiles = new ArrayList<TileData>();

	public DefaultTileSource(final URL url) throws IOException, TileCodeFormatException
	{
		final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

		while (reader.ready())
		{
			final TileData tileData = readTileData(reader);
			this.tiles.add(tileData);
		}

		reader.close();
	}

	@Override
	public List<TileData> getTiles()
	{
		return Collections.unmodifiableList(this.tiles);
	}

	private TileData readTileData(final BufferedReader reader) throws IOException, TileCodeFormatException
	{
		final String imageUrl = reader.readLine();
		final String tileCode = reader.readLine();
		final String[] points = reader.readLine().split(",");

		final Tile tile = new Tile(tileCode);
		final BufferedImage image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(imageUrl));
		final TileData tileData = new TileData(tile, image);

		final Iterator<Section> sections = tile.getSections().iterator();
		for (final String point : points)
		{
			if (sections.hasNext())
			{
				tileData.addSectionPoint(sections.next(), strToPoint2D(point));
			}
			else
			{
				throw new RuntimeException("There are more points than sections in tile [" + tileCode + "].");
			}
		}

		return tileData;
	}

	private Point2D strToPoint2D(final String string)
	{
		final String[] cs = string.split(":");
		final double x = Double.parseDouble(cs[0]);
		final double y = Double.parseDouble(cs[1]);
		return new Point2D.Double(x, y);
	}
}
