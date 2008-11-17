package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.board.InvalidLocationException;
import info.reflectionsofmind.connexion.core.board.Placement;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class GameBoardPanel extends JPanel implements MouseInputListener
{
	private static final long serialVersionUID = 1L;

	private final LocalGuiClient localGuiClient;
	private Location mouseLocation = null;

	public GameBoardPanel(final LocalGuiClient localGuiClient)
	{
		this.localGuiClient = localGuiClient;
		setBorder(BorderFactory.createLineBorder(Color.RED));

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private Game getGame()
	{
		return getClient().getGame();
	}

	private void drawPlacement(final Graphics g, final Placement placement)
	{
		final BufferedImage image = getGame().getTileImage(placement.getTile());
		
		final AffineTransform at = AffineTransform.getQuadrantRotateInstance(placement.getDirection().getIndex(),// 
				image.getWidth() / 2, image.getHeight() / 2);
		
		final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		
		final Location location = (Location) placement.getLocation();

		final int dx1 = getTileLeft(location.getX());
		final int dx2 = getTileLeft(location.getX() + 1);

		final int dy1 = getTileTop(location.getY());
		final int dy2 = getTileTop(location.getY() - 1);

		final int iw = image.getWidth(null);
		final int ih = image.getHeight(null);

		g.drawImage(op.filter(image, null), dx1, dy1, dx2, dy2, 0, 0, iw, ih, null);
	}

	private void drawAllowedMarker(final Graphics g, final Location location)
	{
		final int ts = getTileSide();
		final int x = getTileLeft(location.getX()) + ts / 4;
		final int y = getTileTop(location.getY()) + ts / 4;

		if (getGame().getBoard().isValidLocation(getGame().getCurrentTile(), location, getClient().getDirection()))
		{
			g.drawOval(x, y, ts / 2, ts / 2);
		}

		g.drawString(location.toString(), x, y);
	}

	private void drawMouseMarker(final Graphics g)
	{
		if (getMouseLocation() == null) return;
	
		final int ts = getTileSide();
		final int x = getTileLeft(getMouseLocation().getX());
		final int y = getTileTop(getMouseLocation().getY());
	
		g.drawRect(x, y, ts, ts);
	}

	@Override
	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		for (final Placement placement : getGame().getBoard().getPlacements())
		{
			drawPlacement(g, placement);
		}

		for (final Placement placement : getGame().getBoard().getPlacements())
		{
			for (final IDirection direction : getGame().getBoard().getGeometry().getDirections())
			{
				final Location location = (Location) placement.getLocation().shift(direction);
				drawAllowedMarker(g, location);
			}
		}

		drawMouseMarker(g);
	}

	private int getTileTop(final int y)
	{
		final int ts = getTileSide();
		return getHeight() / 2 - ts / 2 - ts * y;
	}

	private int getTileLeft(final int x)
	{
		final int ts = getTileSide();
		return getWidth() / 2 - ts / 2 + ts * x;
	}

	private int getTileSide()
	{
		int minX = 0, maxX = 0, minY = 0, maxY = 0;

		for (final Placement placement : getGame().getBoard().getPlacements())
		{
			final Location location = (Location) placement.getLocation();
			minX = Math.min(location.getX(), minX);
			maxX = Math.max(location.getX(), maxX);
			minY = Math.min(location.getY(), minY);
			maxY = Math.max(location.getY(), maxY);
		}

		final int tileSideW = getWidth() / 4 / (maxX - minX + 1);
		final int tileSideH = getHeight() / 4 / (maxY - minY + 1);
		return Math.min(tileSideH, tileSideW);
	}

	private Location getLocation(final int x, final int y)
	{
		final int ts = getTileSide();
		final int cx = getWidth() / 2 - ts / 2;
		final int cy = getHeight() / 2 - ts / 2;

		final Geometry geometry = (Geometry) getGame().getBoard().getGeometry();

		return new Location(geometry,// 
				-1 - (int) Math.floor((0.0 + cx - x) / ts),// 
				+1 + (int) Math.floor((0.0 + cy - y) / ts));
	}

	private LocalGuiClient getClient()
	{
		return this.localGuiClient;
	}

	private void setMouseLocation(final Location mouseLocation)
	{
		this.mouseLocation= mouseLocation;
	}

	private Location getMouseLocation()
	{
		return this.mouseLocation;
	}

	// =============================================================================================
	// === MOUSE LISTENER
	// =============================================================================================

	@Override
	public void mouseClicked(final MouseEvent event)
	{
		if (getClient().isTurnMode())
		{
			final Location location = getLocation(event.getX(), event.getY());

			final Turn turn = new Turn(getClient().getPlayer(), getGame().getCurrentTile(), // 
					location, getClient().getDirection(), null, null);

			try
			{
				getClient().getServer().sendTurn(turn);
			}
			catch (final NotYourTurnException exception)
			{
				JOptionPane.showMessageDialog(this, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (InvalidLocationException exception)
			{
				JOptionPane.showMessageDialog(this, "Invalid location!", "Error", JOptionPane.ERROR_MESSAGE);
			}

			getClient().updateInterface();
		}
		else
		{
			JOptionPane.showMessageDialog(this.localGuiClient, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseMoved(final MouseEvent event)
	{
		setMouseLocation(getLocation(event.getX(), event.getY()));
		repaint();
	}

	@Override
	public void mouseExited(final MouseEvent e)
	{
		setMouseLocation(null);
		repaint();
	}

	@Override
	public void mouseDragged(final MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(final MouseEvent e)
	{
	}

	@Override
	public void mousePressed(final MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(final MouseEvent e)
	{
	}
}