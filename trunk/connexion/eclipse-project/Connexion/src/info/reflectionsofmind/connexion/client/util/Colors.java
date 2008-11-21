package info.reflectionsofmind.connexion.client.util;

import java.awt.Color;

public class Colors
{
	private final static Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA };
	
	public static Color getColor(int i)
	{
		return COLORS[i];
	}
}
