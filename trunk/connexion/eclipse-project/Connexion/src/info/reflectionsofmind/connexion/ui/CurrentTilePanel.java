package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.Game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

class CurrentTilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final LocalGuiClient localGuiClient;
	private final StretchingImage tileImage;
	private IDirection rotation = new Geometry().getDirections().get(0);

	public CurrentTilePanel(final LocalGuiClient localGuiClient)
	{
		this.localGuiClient = localGuiClient;
		setBorder(BorderFactory.createLineBorder(Color.RED));
		setLayout(new MigLayout("", "[50]6[50]", "[100]6[12]"));

		this.tileImage = new StretchingImage(getImage());
		add(this.tileImage, "grow, spanx, wrap");

		final JButton rotateRightButton = new JButton(new RotateRightAction("<"));
		rotateRightButton.setFocusable(false);
		add(rotateRightButton, "grow");

		final JButton rotateLeftButton = new JButton(new RotateLeftAction(">"));
		rotateLeftButton.setFocusable(false);
		add(rotateLeftButton, "grow");
	}

	public void updateInterface()
	{
		if (getImage() != null)
		{
			final AffineTransform at = AffineTransform.getQuadrantRotateInstance(getRotation().getIndex(),// 
					getImage().getWidth() / 2, getImage().getHeight() / 2);

			final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

			this.tileImage.setImage(op.filter(getImage(), null));
		}

		repaint();
	}

	private BufferedImage getImage()
	{
		if (getGame() != null)
		{
			return getGame().getTileImage(getGame().getCurrentTile());
		}
		else
		{
			return null;
		}
	}

	public IDirection getRotation()
	{
		return this.rotation;
	}

	private LocalGuiClient getClient()
	{
		return this.localGuiClient;
	}

	private Game getGame()
	{
		return (getClient() == null) ? null : getClient().getGame();
	}

	public void reset()
	{
		this.rotation = new Geometry().getInitialDirection();
	}

	private class RotateLeftAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public RotateLeftAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			CurrentTilePanel.this.rotation = CurrentTilePanel.this.rotation.prev();
			getClient().updateInterface();
		}
	}

	private class RotateRightAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public RotateRightAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			CurrentTilePanel.this.rotation = CurrentTilePanel.this.rotation.next();
			getClient().updateInterface();
		}
	}
}