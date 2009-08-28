package info.reflectionsofmind.connexion.platform.gui.play;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGame;
import info.reflectionsofmind.connexion.platform.gui.play.GameWindow.State;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileSourceUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jvnet.substance.SubstanceLookAndFeel;

class CurrentTilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final BufferedImage emptyImage;
	private BufferedImage meepleImage;

	private final GameWindow clientUI;
	private IDirection rotation = new RectangularGeometry().getDirections().get(0);

	private final StretchingImage tileImage;
	private final JButton rotateRightButton;
	private final JButton rotateLeftButton;
	private final JButton endTurnButton;

	public CurrentTilePanel(final GameWindow localGuiClient)
	{
		this.emptyImage = new BufferedImage(64, 64, BufferedImage.TYPE_BYTE_GRAY);
		
		try
		{
			final String path = "info/reflectionsofmind/connexion/resources/meeple.png";
			final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			this.meepleImage = ImageIO.read(stream);
		}
		catch (final IOException exception)
		{
			this.meepleImage = this.emptyImage;
		}
		
		this.clientUI = localGuiClient;
		setLayout(new MigLayout("", "[65!]6[65!]", "[]6[136!]6[]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.rotateLeftButton = new JButton(new RotateLeftAction("<"));
		this.rotateLeftButton.setFocusable(false);
		this.rotateLeftButton.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, true);
		add(this.rotateLeftButton, "grow");

		this.rotateRightButton = new JButton(new RotateRightAction(">"));
		this.rotateRightButton.setFocusable(false);
		this.rotateRightButton.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, true);
		add(this.rotateRightButton, "grow, wrap");

		this.tileImage = new StretchingImage(getImage());
		add(this.tileImage, "grow, span");

		this.endTurnButton = new JButton(new EndTurnAction());
		this.endTurnButton.setEnabled(false);
		
		add(this.endTurnButton, "span, grow");
	}

	public void updateInterface()
	{
		if (getClientUI().getTurnMode() == State.PLACE_MEEPLE)
		{
			this.endTurnButton.setEnabled(true);
		}
		else
		{
			this.endTurnButton.setEnabled(false);
		}

		final State turnMode = this.clientUI.getTurnMode();

		if ((turnMode == State.PLACE_TILE) || (turnMode == State.WAITING && getImage() != null))
		{
			final int w = getImage().getWidth();
			final int h = getImage().getHeight();
			final int d = this.rotation.getIndex();
	
			final AffineTransform at = getTranslateInstance(0, h);
			at.concatenate(getScaleInstance(1, -1));
			at.concatenate(getQuadrantRotateInstance(-d, w / 2, h / 2));
	
			final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	
			this.rotateLeftButton.setEnabled(true);
			this.rotateRightButton.setEnabled(true);
			this.tileImage.setImage(op.filter(getImage(), null));
		}
		else if (turnMode == State.PLACE_MEEPLE)
		{
			this.rotateLeftButton.setEnabled(false);
			this.rotateRightButton.setEnabled(false);
			this.tileImage.setImage(this.meepleImage);
		}
		else
		{
			this.rotateLeftButton.setEnabled(false);
			this.rotateRightButton.setEnabled(false);
			this.tileImage.setImage(this.emptyImage);
		}

		repaint();
	}

	private BufferedImage getImage()
	{
		if (getGame().getCurrentTile() == null) return null;
		
		final String code = getGame().getCurrentTile().getCode();
		final ITileSource tileSource = getClientUI().getTileSource();
		
		return TileSourceUtil.getTileDataByCode(tileSource, code).getImage();
	}

	public IDirection getDirection()
	{
		return this.rotation;
	}

	private IClientGame getGame()
	{
		return getClientUI().getClient().getGame();
	}

	public void reset()
	{
		this.rotation = new RectangularGeometry().getInitialDirection();
	}
	
	public GameWindow getClientUI()
	{
		return this.clientUI;
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

	private class EndTurnAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public EndTurnAction()
		{
			super("End turn");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			getClientUI().endTurn();
		}
	}
}
