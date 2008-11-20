package info.reflectionsofmind.connexion.tilelist;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileData
{
	private final String code;
	private final BufferedImage image;
	private final List<Point2D> sectionPoints = new ArrayList<Point2D>();
	
	public TileData(String code, BufferedImage image)
	{
		super();
		this.code = code;
		this.image = image;
	}
	
	public void addSectionPoint(Point2D point)
	{
		this.sectionPoints.add(point);
	}
	
	public Point2D getSectionPoint(int sectionIndex)
	{
		return this.sectionPoints.get(sectionIndex);
	}

	public String getCode()
	{
		return this.code;
	}

	public BufferedImage getImage()
	{
		return this.image;
	}
}
