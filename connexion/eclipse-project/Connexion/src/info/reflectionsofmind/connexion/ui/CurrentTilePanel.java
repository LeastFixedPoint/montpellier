package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Direction;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

class CurrentTilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final LocalGuiClient localGuiClient;
	private final StretchingImage tileImage;
	private Direction rotation;

	public CurrentTilePanel(final LocalGuiClient localGuiClient)
	{
		this.localGuiClient = localGuiClient;
		setBorder(BorderFactory.createLineBorder(Color.RED));
		setLayout(new MigLayout("", "[50]6[50]", "[100]6[12]"));

		this.tileImage = new StretchingImage(getIcon());
		add(this.tileImage, "grow, spanx, wrap");

		final JButton rotateLeftButton = new JButton("<");
		rotateLeftButton.setFocusable(false);
		add(rotateLeftButton, "grow");

		final JButton rotateRightButton = new JButton(">");
		rotateRightButton.setFocusable(false);
		add(rotateRightButton, "grow");
	}

	public void updateInterface()
	{
		if (this.tileImage != null)
		{
			this.tileImage.setIcon(getIcon());
		}
	}

	private ImageIcon getIcon()
	{
		if (this.localGuiClient.getGame() != null)
		{
			return new ImageIcon(this.localGuiClient.getGame().getTileImageURL(this.localGuiClient.getGame().getCurrentTile()));
		}
		else
		{
			return new ImageIcon();
		}
	}

	public Direction getRotation()
	{
		return this.rotation;
	}
}