package info.reflectionsofmind.connexion.gui.play;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class StretchingImage extends JComponent
{
	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	public StretchingImage(final BufferedImage image)
	{
		this.image = image;
	}

	public StretchingImage()
	{
	}

	@Override
	protected void paintComponent(final Graphics graphics)
	{
		super.paintComponent(graphics);
		
		if (getImage() != null)
		{
			graphics.drawImage(getImage(), 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	public BufferedImage getImage()
	{
		return this.image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
		repaint();
	}
}
