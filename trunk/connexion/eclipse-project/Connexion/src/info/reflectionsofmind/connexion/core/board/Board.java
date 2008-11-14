package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.IOffset;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Board
{
	private final IGeometry geometry;
	private final BiMap<ILocation, Tile> tiles = new HashBiMap<ILocation, Tile>();
	private final BiMap<Meeple, Section> meeples = new HashBiMap<Meeple, Section>();
	private final List<Feature> features = new ArrayList<Feature>();

	public Board(IGeometry geometry, Tile initialTile)
	{
		this.geometry = geometry;
		placeTile(initialTile, this.geometry.getInitialLocation());
	}
	
	public void placeTile(final Tile tile, final ILocation location)
	{
		if (!isValidLocation(tile, location)) throw new RuntimeException();

		this.tiles.put(location, tile);

		for (final Side side : tile.getSides())
		{
			joinSides(side, getAdjacentSide(side));
		}
	}
	
	public void placeMeeple(final Meeple meeple, final Section section)
	{
		// TODO Place meeple
	}

	private void joinSides(final Side currentSide, final Side adjacentSide)
	{
		final Iterator<Section> currentSectionIterator = currentSide.getSections().iterator();
		final ListIterator<Section> adjacentSectionIterator = adjacentSide.getSections().listIterator(adjacentSide.getSections().size());

		while (currentSectionIterator.hasNext() && adjacentSectionIterator.hasPrevious())
		{
			joinSections(currentSectionIterator.next(), adjacentSectionIterator.previous());
		}

		if (currentSectionIterator.hasNext() || adjacentSectionIterator.hasPrevious())
		{
			throw new RuntimeException("Sides [" + currentSide + "] and [" + adjacentSide + "] have different number of sections!");
		}
	}

	private void joinSections(final Section currentSection, final Section adjacentSection)
	{
		final Feature currentFeature = getFeatureOf(currentSection);
		final Feature adjacentFeature = getFeatureOf(currentSection);

		if (currentFeature != null)
		{
			adjacentFeature.addSection(currentSection);
		}
		else
		{
			mergeFeatures(currentFeature, adjacentFeature);
		}
	}

	private void mergeFeatures(final Feature currentFeature, final Feature adjacentFeature)
	{
		this.features.remove(currentFeature);

		for (final Section section : currentFeature.getSections())
		{
			adjacentFeature.addSection(section);
		}
	}

	private boolean isValidLocation(final Tile tile, final ILocation location)
	{
		if (!hasNeighbouringTiles(tile, location)) return false;

		if (!compartibleSides(tile, location)) return false;

		return true;
	}

	private boolean hasNeighbouringTiles(final Tile tile, final ILocation location)
	{
		for (final Side side : tile.getSides())
		{
			if (getAdjacentSide(side, location) != null) return true;
		}

		return false;
	}

	private boolean compartibleSides(final Tile tile, final ILocation location)
	{
		for (final Side currentSide : tile.getSides())
		{
			final Side adjacentSide = getAdjacentSide(currentSide, location);
			
			if (adjacentSide != null)
			{
				final Iterator<Section> currentSectionIterator = currentSide.getSections().iterator();
				final ListIterator<Section> adjacentSectionIterator = adjacentSide.getSections().listIterator(adjacentSide.getSections().size());

				while (currentSectionIterator.hasNext() && adjacentSectionIterator.hasPrevious())
				{
					if (currentSectionIterator.next().getType() != adjacentSectionIterator.previous().getType()) return false;
				}

				if (currentSectionIterator.hasNext() || adjacentSectionIterator.hasPrevious())
				{
					return false;
				}
			}
		}
		
		return true;
	}

	private Feature getFeatureOf(final Section section)
	{
		for (final Feature feature : this.features)
		{
			if (feature.getSections().contains(section))
			{
				return feature;
			}
		}

		return null;
	}

	private Side getAdjacentSide(final Side side)
	{
		return getAdjacentSide(side, this.tiles.inverse().get(side.getTile()));
	}

	private Side getAdjacentSide(final Side side, final ILocation location)
	{
		final IOffset offset = side.getOffset();
		final Tile adjacentTile = this.tiles.get(location.offset(offset));
		return adjacentTile == null ? null : adjacentTile.getSideAt(location.getAdjacentOffset(offset));
	}
}
