package info.reflectionsofmind.connexion.core.game.sequence;

import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomTileSequence implements ITileSequence
{
	private final Iterator<Tile> iterator;
	private final int totalNumberOfTiles;
	private Tile currentTile = null;

	public RandomTileSequence(final List<Tile> tiles)
	{
		final List<Tile> shuffledTiles = new ArrayList<Tile>(tiles);
		Collections.shuffle(shuffledTiles);
		this.iterator = shuffledTiles.iterator();
		this.totalNumberOfTiles = shuffledTiles.size();
		this.currentTile = iterator.next();
	}

	@Override
	public boolean hasMoreTiles()
	{
		return this.iterator.hasNext();
	}

	@Override
	public void nextTile()
	{
		this.currentTile = iterator.next();
	}
	
	@Override
	public Tile getCurrentTile()
	{
		return currentTile;
	}

	@Override
	public Integer getTotalNumberOfTiles()
	{
		return this.totalNumberOfTiles;
	}
}
