package info.reflectionsofmind.connexion.tilelist;

import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TileData
{
	private final Tile tile;
	private final BufferedImage image;
	private final Map<Section, DoublePoint> sectionPoints = new HashMap<Section, DoublePoint>();
	
	public TileData(Tile tile, BufferedImage image)
	{
		super();
		this.tile = tile;
		this.image = image;
	}
	
	public void addSectionPoint(Section section, DoublePoint point)
	{
		this.sectionPoints.put(section, point);
	}
	
	public DoublePoint getSectionPoint(Section section)
	{
		return this.sectionPoints.get(section);
	}

	public Tile getTile()
	{
		return this.tile;
	}

	public BufferedImage getImage()
	{
		return this.image;
	}
}
