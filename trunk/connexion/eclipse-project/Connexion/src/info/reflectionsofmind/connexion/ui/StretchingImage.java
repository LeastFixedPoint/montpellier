package info.reflectionsofmind.connexion.ui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class StretchingImage extends JComponent
{
	private static final long serialVersionUID = 1L;
	private ImageIcon icon;

	public StretchingImage(final ImageIcon icon)
	{
		this.icon = icon;
	}

	public StretchingImage()
	{
	}

	@Override
	protected void paintComponent(final Graphics graphics)
	{
		super.paintComponent(graphics);
		
		if (this.icon != null)
		{
			graphics.drawImage(getIcon().getImage(), 0, 0, getWidth(), getHeight(), null);
		}
	}

	public ImageIcon getIcon()
	{
		return this.icon;
	}

	public void setIcon(final ImageIcon icon)
	{
		this.icon = icon;
		this.repaint();
	}
}
