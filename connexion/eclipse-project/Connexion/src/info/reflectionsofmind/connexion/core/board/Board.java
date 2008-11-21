package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.exception.MeeplePlacementException;
import info.reflectionsofmind.connexion.core.board.exception.NotLastTileMeepleException;
import info.reflectionsofmind.connexion.core.board.exception.TilePlacementException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Board
{
	private final IGeometry geometry;
	private final List<TilePlacement> placements = new ArrayList<TilePlacement>();
	private final BiMap<Meeple, Section> meeples = new HashBiMap<Meeple, Section>();
	private final List<Feature> features = new ArrayList<Feature>();

	public Board(final IGeometry geometry)
	{
		this.geometry = geometry;
	}

	// ============================================================================================
	// === TILE PLACEMENT
	// ============================================================================================

	public void placeTile(final Tile tile, final ILocation location, final IDirection direction) throws TilePlacementException
	{
		final TilePlacement placement = new TilePlacement(this, tile, direction, location);

		if (!getPlacements().isEmpty())
		{
			if (!BoardUtil.isValidLocation(this, tile, direction, location)) throw new InvalidTileLocationException(placement);
		}

		this.placements.add(placement);
		this.features.addAll(createFeatures(tile));

		for (final Side side : placement.getSides())
		{
			final Side opposingSide = BoardUtil.getOpposingSide(this, placement, side);

			if (opposingSide != null)
			{
				joinSides(side, opposingSide);
			}
		}
	}

	public void placeMeeple(final Meeple meeple, final Section section) throws MeeplePlacementException
	{
		if (!getPlacements().get(getPlacements().size() - 1).getTile().getSections().contains(section))
		{
			throw new NotLastTileMeepleException();
		}

		for (Section featureSection : BoardUtil.getFeatureOf(this, section).getSections())
		{
			if (this.meeples.inverse().containsKey(featureSection))
			{
				throw new FeatureTakenException();
			}
		}

		this.meeples.put(meeple, section);
	}

	public void removeMeeple(Meeple meeple)
	{
		this.meeples.remove(meeple);
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
			final Feature currentFeature = BoardUtil.getFeatureOf(this, currentSectionIterator.next());
			final Feature adjacentFeature = BoardUtil.getFeatureOf(this, adjacentSectionIterator.previous());

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

	public IGeometry getGeometry()
	{
		return this.geometry;
	}

	public List<Feature> getFeatures()
	{
		return Collections.unmodifiableList(this.features);
	}

	public List<TilePlacement> getPlacements()
	{
		return Collections.unmodifiableList(this.placements);
	}

	public List<Meeple> getMeeples()
	{
		return Collections.unmodifiableList(new ArrayList<Meeple>(this.meeples.keySet()));
	}

	public Section getMeepleSection(Meeple meeple)
	{
		return this.meeples.get(meeple);
	}
}
