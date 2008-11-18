package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;

public class BoardUtil
{
	private BoardUtil()
	{
		throw new UnsupportedOperationException();
	}
	
	public static int getMinX(Board board)
	{
		int minX = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			minX = Math.min(((Location) placement.getLocation()).getX(), minX);
		}
		
		return minX;
	}
	
	public static int getMaxX(Board board)
	{
		int maxX = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			maxX = Math.max(((Location) placement.getLocation()).getX(), maxX);
		}
		
		return maxX;
	}
	
	public static int getMinY(Board board)
	{
		int minY = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			minY = Math.min(((Location) placement.getLocation()).getY(), minY);
		}
		
		return minY;
	}
	
	public static int getMaxY(Board board)
	{
		int maxY = 0;

		for (final TilePlacement placement : board.getPlacements())
		{
			maxY = Math.max(((Location) placement.getLocation()).getY(), maxY);
		}
		
		return maxY;
	}
}
