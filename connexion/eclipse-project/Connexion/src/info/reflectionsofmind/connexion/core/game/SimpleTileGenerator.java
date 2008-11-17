package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class SimpleTileGenerator implements ITileGenerator
{
	private final Map<Tile, BufferedImage> images = new HashMap<Tile, BufferedImage>();
	private final List<Tile> sequence = new ArrayList<Tile>();
	private final Iterator<Tile> iterator;

	public SimpleTileGenerator(final URL url) throws IOException, TileCodeFormatException
	{
		final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

		while (reader.ready())
		{
			final String imageUrl = reader.readLine();
			final String tileCode = reader.readLine();

			final Tile tile = new Tile(tileCode);
			this.images.put(tile, ImageIO.read(new URL(imageUrl)));
		}

		this.sequence.addAll(this.images.keySet());
		Collections.shuffle(this.sequence);
		this.iterator = this.sequence.iterator();

		reader.close();
	}

	@Override
	public boolean hasMoreTiles()
	{
		return this.iterator.hasNext();
	}

	@Override
	public Tile nextTile()
	{
		return this.iterator.next();
	}

	@Override
	public BufferedImage getTileImage(final Tile tile)
	{
		return this.images.get(tile);
	}
}
