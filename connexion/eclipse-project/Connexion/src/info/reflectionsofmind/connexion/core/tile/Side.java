package info.reflectionsofmind.connexion.core.tile;

import info.reflectionsofmind.connexion.core.location.IOffset;
import info.reflectionsofmind.connexion.core.util.Link;

import java.util.Collections;
import java.util.List;

public class Side
{
	private final Link<Side, Tile> tile = new Link<Side, Tile>(this);
	private final List<Section> sections;
	private final IOffset offset;

	public Side(final Tile tile, final IOffset offset, final List<Section> sections)
	{
		this.tile.link(tile.getSides());
		this.offset = offset;
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
	
	public IOffset getOffset()
	{
		return this.offset;
	}
}
