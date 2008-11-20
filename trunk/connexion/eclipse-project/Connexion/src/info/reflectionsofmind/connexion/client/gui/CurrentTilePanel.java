package info.reflectionsofmind.connexion.client.gui;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.client.gui.ClientUI.State;
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
	private static final BufferedImage FINISHED_IMAGE = new BufferedImage(64, 64, BufferedImage.TYPE_BYTE_GRAY);

	private final ClientUI clientUI;
	private IDirection rotation = new Geometry().getDirections().get(0);

	private final StretchingImage tileImage;
	private final JButton rotateRightButton;
	private final JButton rotateLeftButton;

	public CurrentTilePanel(final ClientUI localGuiClient)
	{
		this.clientUI = localGuiClient;
		setLayout(new MigLayout("", "[45!]6[45!]", "[96!]6[30!]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.tileImage = new StretchingImage(getImage());
		add(this.tileImage, "grow, span");

		rotateRightButton = new JButton(new RotateRightAction("<"));
		rotateRightButton.setFocusable(false);
		add(rotateRightButton, "grow");

		rotateLeftButton = new JButton(new RotateLeftAction(">"));
		rotateLeftButton.setFocusable(false);
		add(rotateLeftButton, "grow");
	}

	public void updateInterface()
	{
		final State turnMode = this.clientUI.getTurnMode();

		if (turnMode == State.PLACE_TILE || turnMode == State.WAITING)
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
		else
		{
			this.rotateLeftButton.setEnabled(false);
			this.rotateRightButton.setEnabled(false);
			this.tileImage.setImage(getFinishedImage());
		}

		repaint();
	}

	private BufferedImage getFinishedImage()
	{
		return FINISHED_IMAGE;
	}

	private BufferedImage getImage()
	{
		return TileSourceUtil.getTileData(this.clientUI.getClient().getTileSource(), getGame().getCurrentTile()).getImage();
	}

	public OrientedTile getOrientedTile()
	{
		return new OrientedTile(getGame().getCurrentTile(), this.rotation);
	}

	private Game getGame()
	{
		return this.clientUI.getClient().getGame();
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
			CurrentTilePanel.this.clientUI.updateInterface();
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
			CurrentTilePanel.this.clientUI.updateInterface();
		}
	}
}
