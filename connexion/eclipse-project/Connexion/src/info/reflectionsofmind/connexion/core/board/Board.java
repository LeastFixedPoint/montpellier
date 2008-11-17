package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Board
{
	private final IGeometry geometry;
	private final List<TilePlacement> placements = new ArrayList<TilePlacement>();
	private final BiMap<Meeple, Section> meeples = new HashBiMap<Meeple, Section>();
	private final List<Feature> features = new ArrayList<Feature>();

	public Board(final IGeometry geometry, final Tile initialTile)
	{
		this.geometry = geometry;
		placeInitialTile(initialTile);
	}

	// ============================================================================================
	// === TILE PLACEMENT
	// ============================================================================================

	private void placeInitialTile(final Tile initialTile)
	{
		final OrientedTile orientedTile = new OrientedTile(initialTile, getGeometry().getDirections().get(0));
		final TilePlacement placement = new TilePlacement(this, orientedTile, getGeometry().getInitialLocation());

		this.placements.add(placement);
		this.features.addAll(createFeatures(initialTile));
	}

	public void placeTile(final Tile tile, final ILocation location, final IDirection direction) throws InvalidTileLocationException
	{
		final OrientedTile orientedTile = new OrientedTile(tile, direction);
		final TilePlacement placement = new TilePlacement(this, orientedTile, location);

		if (!isValidLocation(orientedTile, location)) throw new InvalidTileLocationException(placement);

		this.placements.add(placement);
		this.features.addAll(createFeatures(tile));

		for (final Side side : placement.getSides())
		{
			final Side opposingSide = getOpposingSide(placement, side);

			if (opposingSide != null)
			{
				joinSides(side, opposingSide);
			}
		}
	}

	private List<Feature> createFeatures(final Tile tile)
	{
		final List<Feature> features = new ArrayList<Feature>();

		for (final Section section : tile.getSections())
		{
			final Feature feature = new Feature(this);
			feature.addSection(section);
			features.add(feature);
		}

		return features;
	}

	private void joinSides(final Side currentSide, final Side adjacentSide)
	{
		final Iterator<Section> currentSectionIterator = currentSide.getSections().iterator();
		final ListIterator<Section> adjacentSectionIterator = adjacentSide.getSections().listIterator(adjacentSide.getSections().size());

		while (currentSectionIterator.hasNext() && adjacentSectionIterator.hasPrevious())
		{
			final Feature currentFeature = getFeatureOf(currentSectionIterator.next());
			final Feature adjacentFeature = getFeatureOf(adjacentSectionIterator.previous());

			mergeFeatures(currentFeature, adjacentFeature);
		}

		if (currentSectionIterator.hasNext() || adjacentSectionIterator.hasPrevious())
		{
			throw new RuntimeException("Sides [" + currentSide + "] and [" + adjacentSide + "] have different number of sections!");
		}

		checkForCompletedFeatures();
	}

	private void mergeFeatures(final Feature currentFeature, final Feature adjacentFeature)
	{
		if (currentFeature == adjacentFeature) return;

		this.features.remove(currentFeature);

		for (final Section section : currentFeature.getSections())
		{
			adjacentFeature.addSection(section);
		}
	}

	private void checkForCompletedFeatures()
	{
		for (Feature feature : getFeatures())
		{
			if (feature.isCompleted())
			{
				final List<Meeple> featureMeeples = new ArrayList<Meeple>();

				for (Entry<Meeple, Section> entry : meeples.entrySet())
				{
					if (feature.getSections().contains(entry.getValue()))
					{
						featureMeeples.add(entry.getKey());
					}
				}
				
				
			}
		}
	}

	// ============================================================================================
	// === MEEPLE PLACEMENT
	// ============================================================================================

	public void placeMeeple(final Meeple meeple, final Section section) throws FeatureTakenException
	{
		for (Section featureSection : getFeatureOf(section).getSections())
		{
			if (this.meeples.inverse().containsKey(featureSection))
			{
				throw new FeatureTakenException();
			}
		}

		this.meeples.put(meeple, section);
	}

	// ============================================================================================
	// === TILE PLACEMENT VALIDATION
	// ============================================================================================

	public boolean isValidLocation(final OrientedTile orientedTile, final ILocation location)
	{
		if (getPlacementAt(location) != null) return false;
		if (!hasNeighbouringTiles(location)) return false;
		if (!compartibleSides(orientedTile, location)) return false;

		return true;
	}

	private boolean hasNeighbouringTiles(final ILocation location)
	{
		for (final IDirection direction : getGeometry().getDirections())
		{
			final ILocation neighbour = location.shift(direction);
			if (getPlacementAt(neighbour) != null) return true;
		}

		return false;
	}

	private boolean compartibleSides(final OrientedTile orientedTile, final ILocation location)
	{
		final TilePlacement placement = new TilePlacement(this, orientedTile, location);

		for (final Side currentSide : placement.getSides())
		{
			final Side adjacentSide = getOpposingSide(placement, currentSide);

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

	// ============================================================================================
	// === GET...OF... METHODS
	// ============================================================================================

	public Feature getFeatureOf(final Section section)
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

	public ILocation getLocationOf(final Tile tile)
	{
		for (final TilePlacement placement : getPlacements())
		{
			if (placement.getTile() == tile) return placement.getLocation();
		}

		return null;
	}

	public TilePlacement getPlacementAt(final ILocation location)
	{
		for (final TilePlacement placement : getPlacements())
		{
			if (placement.getLocation().equals(location)) return placement;
		}

		return null;
	}

	public TilePlacement getPlacementOf(final Tile tile)
	{
		for (final TilePlacement placement : getPlacements())
		{
			if (placement.getTile() == tile) return placement;
		}

		return null;
	}

	public Side getOpposingSide(final TilePlacement placement, final Side side)
	{
		final IDirection direction = placement.getDirectionOfSide(side);
		final IDirection opposingDirection = placement.getLocation().getOpposingDirection(direction);
		final TilePlacement opposingPlacement = getPlacementAt(placement.getLocation().shift(direction));
		return opposingPlacement == null ? null : opposingPlacement.getSideForDirection(opposingDirection);
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public IGeometry getGeometry()
	{
		return this.geometry;
	}

	public List<Feature> getFeatures()
	{
		return this.features;
	}

	public List<TilePlacement> getPlacements()
	{
		return this.placements;
	}

	public List<Section> getAdjacentSections(Section section)
	{
		final List<Section> sections = new ArrayList<Section>();

		for (Side currentSide : section.getTile().getSides())
		{
			if (currentSide.getSections().contains(section))
			{
				final Side oppositeSide = getOpposingSide(getPlacementOf(section.getTile()), currentSide);

				if (oppositeSide != null)
				{
					final Iterator<Section> currentSectionIterator = currentSide.getSections().iterator();
					final ListIterator<Section> oppositeSectionIterator = oppositeSide.getSections().listIterator(oppositeSide.getSections().size());

					while (currentSectionIterator.hasNext() && oppositeSectionIterator.hasPrevious())
					{
						if (currentSectionIterator.next() == section)
						{
							sections.add(oppositeSectionIterator.previous());
						}
						else
						{
							oppositeSectionIterator.next();
						}
					}
				}
			}
		}

		return sections;
	}
}
