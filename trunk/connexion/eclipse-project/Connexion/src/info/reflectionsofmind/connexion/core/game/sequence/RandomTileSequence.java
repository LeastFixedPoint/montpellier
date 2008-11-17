package info.reflectionsofmind.connexion.core.game.sequence;

import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomTileSequence implements ITileSequence
{
	private final Iterator<Tile> iterator;

	public RandomTileSequence(final List<Tile> tiles)
	{
		final List<Tile> shuffledTiles = new ArrayList<Tile>(tiles);
		Collections.shuffle(shuffledTiles);
		this.iterator = shuffledTiles.iterator();
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

}
