package info.reflectionsofmind.connexion.fortress.core.common.tile;

import info.reflectionsofmind.connexion.fortress.core.util.Link;
import info.reflectionsofmind.connexion.fortress.core.util.Multi;

public class Section
{
	private final Type type;
	private final Link<Section, Tile> tile = new Link<Section, Tile>(this);
	private final Multi<Section, Section> adjacents = new Multi<Section, Section>(this);

	public Section(final Type type, final Tile tile)
	{
		this.type = type;
		this.tile.link(tile.getSections());
	}

	public void addAdjacent(final Section section)
	{
		if (isAdjacentTo(section)) return;
		adjacents.link(section.adjacents);
	}

	public boolean isAdjacentTo(final Section section)
	{
		return this.adjacents.contains(section);
	}

	public Tile getTile()
	{
		return this.tile.get();
	}

	public Type getType()
	{
		return this.type;
	}
}
