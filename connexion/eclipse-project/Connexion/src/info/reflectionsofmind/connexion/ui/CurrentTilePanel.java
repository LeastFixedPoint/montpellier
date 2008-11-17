package info.reflectionsofmind.connexion.ui;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.tilelist.TileSourceUtil;

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
			final int w = getImage().getWidth();
			final int h = getImage().getHeight();
			final int d = this.rotation.getIndex();

			final AffineTransform at = getTranslateInstance(0, h);
			at.concatenate(getScaleInstance(1, -1));
			at.concatenate(getQuadrantRotateInstance(-d, w / 2, h / 2));

			final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

			this.tileImage.setImage(op.filter(getImage(), null));
		}

		repaint();
	}

	private BufferedImage getImage()
	{
		if (getGame() != null)
		{
			return TileSourceUtil.getTileData(getClient().getServer().getTileSource(), getGame().getCurrentTile()).getImage();
		}
		else
		{
			return null;
		}
	}

	public OrientedTile getOrientedTile()
	{
		return new OrientedTile(getGame().getCurrentTile(), this.rotation);
	}

	private LocalGuiClient getClient()
	{
		return this.localGuiClient;
	}

	private Game getGame()
	{
		return getClient().getServer().getGame();
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