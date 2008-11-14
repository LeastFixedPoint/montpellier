package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleTileGenerator implements ITileGenerator
{
	private final Map<Tile, URL> images = new HashMap<Tile, URL>();
	private final List<Tile> sequence = new ArrayList<Tile>();
	private final Iterator<Tile> iterator;

	public SimpleTileGenerator(final File file) throws IOException, TileCodeFormatException
	{
		final BufferedReader reader = new BufferedReader(new FileReader(file));

		while (reader.ready())
		{
			final String imageUrl = reader.readLine();
			final String tileCode = reader.readLine();

			final Tile tile = new Tile(tileCode);
			this.images.put(tile, new URL(imageUrl));
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
	public URL getImage(final Tile tile)
	{
		return this.images.get(tile);
	}
}
