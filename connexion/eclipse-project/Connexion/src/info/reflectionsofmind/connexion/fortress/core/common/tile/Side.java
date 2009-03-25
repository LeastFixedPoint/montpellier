package info.reflectionsofmind.connexion.fortress.core.common.tile;

import info.reflectionsofmind.connexion.fortress.core.common.util.Link;

import java.util.Collections;
import java.util.List;

public class Side
{
	private final Link<Side, Tile> tile = new Link<Side, Tile>(this);
	private final List<Section> sections;

	public Side(final Tile tile, final List<Section> sections)
	{
		this.tile.link(tile.getSidesLink());
		this.sections = Collections.unmodifiableList(sections);
	}

	public Tile getTile()
	{
		return this.tile.get();
	}

	public List<Section> getSections()
	{
		return this.sections;
	}
}
