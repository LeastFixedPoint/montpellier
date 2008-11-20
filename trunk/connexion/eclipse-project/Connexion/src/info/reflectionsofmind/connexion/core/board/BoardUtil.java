package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BoardUtil
{
	private BoardUtil()
	{
		throw new UnsupportedOperationException();
	}

	public static int getMinX(final Board board)
	{
		int minX = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			minX = Math.min(((Location) placement.getLocation()).getX(), minX);
		}

		return minX;
	}

	public static int getMaxX(final Board board)
	{
		int maxX = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			maxX = Math.max(((Location) placement.getLocation()).getX(), maxX);
		}

		return maxX;
	}

	public static int getMinY(final Board board)
	{
		int minY = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			minY = Math.min(((Location) placement.getLocation()).getY(), minY);
		}

		return minY;
	}

	public static int getMaxY(final Board board)
	{
		int maxY = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			maxY = Math.max(((Location) placement.getLocation()).getY(), maxY);
		}

		return maxY;
	}

	public static Feature getFeatureOf(final Board board, final Section section)
	{
		for (final Feature feature : board.getFeatures())
		{
			if (feature.getSections().contains(section))
			{
				return feature;
			}
		}

		return null;
	}

	public static ILocation getLocationOf(final Board board, final Tile tile)
	{
		for (final TilePlacement placement : board.getPlacements())
		{
			if (placement.getTile() == tile) return placement.getLocation();
		}

		return null;
	}

	public static TilePlacement getPlacementAt(final Board board, final ILocation location)
	{
		for (final TilePlacement placement : board.getPlacements())
		{
			if (placement.getLocation().equals(location)) return placement;
		}

		return null;
	}

	public static TilePlacement getPlacementOf(final Board board, final Tile tile)
	{
		for (final TilePlacement placement : board.getPlacements())
		{
			if (placement.getTile() == tile) return placement;
		}

		return null;
	}

	public static Side getOpposingSide(final Board board, final TilePlacement placement, final Side side)
	{
		final IDirection direction = placement.getDirectionOfSide(side);
		final IDirection opposingDirection = placement.getLocation().getOpposingDirection(direction);
		final TilePlacement opposingPlacement = BoardUtil.getPlacementAt(board, placement.getLocation().shift(direction));
		return opposingPlacement == null ? null : opposingPlacement.getSideForDirection(opposingDirection);
	}

	public static List<Section> getAdjacentSections(final Board board, final Section section)
	{
		final List<Section> sections = new ArrayList<Section>();

		for (final Side currentSide : section.getTile().getSides())
		{
			if (currentSide.getSections().contains(section))
			{
				final Side oppositeSide = BoardUtil.getOpposingSide(board, BoardUtil.getPlacementOf(board, section.getTile()), currentSide);

				if (oppositeSide != null)
				{
					final int index = currentSide.getSections().indexOf(section);
					sections.add(oppositeSide.getSections().get(oppositeSide.getSections().size() - index - 1));
				}
			}
		}

		return sections;
	}

	public static boolean isValidLocation(final Board board, final OrientedTile orientedTile, final ILocation location)
	{
		if (BoardUtil.getPlacementAt(board, location) != null) return false;
		if (!BoardUtil.hasNeighbouringTiles(board, location)) return false;
		if (!BoardUtil.compartibleSides(board, orientedTile, location)) return false;

		return true;
	}

	private static boolean hasNeighbouringTiles(final Board board, final ILocation location)
	{
		for (final IDirection direction : board.getGeometry().getDirections())
		{
			final ILocation neighbour = location.shift(direction);
			if (BoardUtil.getPlacementAt(board, neighbour) != null) return true;
		}

		return false;
	}

	private static boolean compartibleSides(final Board board, final OrientedTile orientedTile, final ILocation location)
	{
		final TilePlacement placement = new TilePlacement(board, orientedTile, location);

		for (final Side currentSide : placement.getSides())
		{
			final Side adjacentSide = BoardUtil.getOpposingSide(board, placement, currentSide);

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
}
