package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Feature
{
	private final List<Section> sections = new ArrayList<Section>();
	private final Board board;

	public Feature(final Board board)
	{
		this.board = board;
	}

	public List<Section> getSections()
	{
		return Collections.unmodifiableList(this.sections);
	}

	public void addSection(final Section currentSection)
	{
		this.sections.add(currentSection);
	}

	public boolean isCompleted()
	{
		for (final Section section : this.sections)
		{
			if (isOpen(section))
			{
				return false;
			}
		}

		return true;
	}

	private boolean isOpen(final Section section)
	{
		for (final Side side : section.getTile().getSides())
		{
			final TilePlacement placement = getBoard().getPlacementOf(side.getTile());
			final Side opposingSide = getBoard().getOpposingSide(placement, side);
			if (side.getSections().contains(section) && opposingSide == null)
			{
				return true;
			}
		}

		return false;
	}

	public Board getBoard()
	{
		return this.board;
	}
}
