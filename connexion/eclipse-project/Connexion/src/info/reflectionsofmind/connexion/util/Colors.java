package info.reflectionsofmind.connexion.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Colors
{
	private final static Color[] COLORS = new Color[] { //
	Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.WHITE };

	public static Color getColor(final int i)
	{
		return COLORS[i];
	}
	
	public static Icon getEmptyIcon(final int size)
	{
		final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = (Graphics2D) image.getGraphics();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, size - 1, size - 1);

		return new ImageIcon(image);
	}

	public static Icon getIcon(final int i, final int size)
	{
		final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = (Graphics2D) image.getGraphics();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, size - 1, size - 1);

		graphics.setColor(getColor(i));
		graphics.fillRect(1, 1, size - 2, size - 2);

		return new ImageIcon(image);
	}
}
