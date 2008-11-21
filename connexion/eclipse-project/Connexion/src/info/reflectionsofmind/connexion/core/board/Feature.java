package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.Type;

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
			final TilePlacement placement = BoardUtil.getPlacementOf(getBoard(), side.getTile());
			final Side opposingSide = BoardUtil.getOpposingSide(getBoard(), placement, side);
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

	public int getCurrentScore()
	{
		int score = 0;
		
		if (getSections().get(0).getType() == Type.CASTLE)
		{
			final List<Tile> tiles = new ArrayList<Tile>();
			
			for (Section section : getSections())
			{
				if (tiles.contains(section.getTile())) continue; // Do not account two sections on same tile
				tiles.add(section.getTile());
				score += 1;
			}
		}
		
		return score;
	}

	public int getCompletedScore()
	{
		int score = 0;
		
		if (getType() == Type.CASTLE)
		{
			final List<Tile> tiles = new ArrayList<Tile>();
			
			for (Section section : getSections())
			{
				if (tiles.contains(section.getTile())) continue; // Do not account two sections on same tile
				tiles.add(section.getTile());
				score += 2;
			}
		}
		
		return score;
	}

	public Type getType()
	{
		return getSections().get(0).getType();
	}
}
