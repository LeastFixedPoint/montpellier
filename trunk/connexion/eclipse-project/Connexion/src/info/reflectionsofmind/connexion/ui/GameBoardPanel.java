package info.reflectionsofmind.connexion.ui;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.core.board.InvalidLocationException;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.Placement;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.tilelist.DoublePoint;
import info.reflectionsofmind.connexion.tilelist.TileData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
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
	private Point mousePoint = null;

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
		final TileData tileData = getGame().getTileData(placement.getTile());
		final BufferedImage image = tileData.getImage();

		final int d = placement.getDirection().getIndex();
		final int w = image.getWidth();
		final int h = image.getHeight();

		final AffineTransform at = getQuadrantRotateInstance(d, w / 2, h / 2);
		at.concatenate(getTranslateInstance(0, h));
		at.concatenate(getScaleInstance(1, -1));

		final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		final Location location = (Location) placement.getLocation();

		final int x1 = getTileLeft(location.getX());
		final int x2 = getTileLeft(location.getX() + 1);

		final int y1 = getTileTop(location.getY());
		final int y2 = getTileTop(location.getY() + 1);

		g.drawImage(op.filter(image, null), x1, y1, x2, y2, 0, 0, w, h, null);

		for (final Section section : tileData.getTile().getSections())
		{
			final Point point = getSectionPoint(section);

			g.drawRect(point.x - 2, point.y - 2, 4, 4);
		}
	}

	private Point getSectionPoint(final Section section)
	{
		final Placement placement = getGame().getBoard().getPlacementOf(section.getTile());
		final Location location = (Location) placement.getLocation();
		final TileData tileData = getGame().getTileData(section.getTile());
		final DoublePoint point = tileData.getSectionPoint(section);

		final int x1 = getTileLeft(location.getX());
		final int x2 = getTileLeft(location.getX() + 1);

		final int y1 = getTileTop(location.getY());
		final int y2 = getTileTop(location.getY() + 1);

		final int ts = getTileSide();

		int dx = (int) ((x2 - x1) * point.getX());
		int dy = (int) ((y2 - y1) * point.getY());

		for (int i = 0; i < placement.getDirection().getIndex(); i++)
		{
			final int tmp = dx;
			dx = dy;
			dy = ts - tmp;
		}

		dy = ts - dy;

		return new Point(x1 + dx, y1 + dy);
	}

	private void drawAllowedMarker(final Graphics g, final Location location)
	{
		final int ts = getTileSide();
		final int x = getTileLeft(location.getX()) + ts / 4;
		final int y = getTileTop(location.getY()) + ts / 4;

		final OrientedTile orientedTile = getClient().getCurrentTilePanel().getOrientedTile();

		g.setColor(Color.black);

		if (getGame().getBoard().isValidLocation(orientedTile, location))
		{
			g.drawOval(x, y, ts / 2, ts / 2);
		}
	}

	private void drawMouseMarker(final Graphics g)
	{
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (getMouseLocation() == null) return;

		final int ts = getTileSide();
		final int x = getTileLeft(getMouseLocation().getX());
		final int y = getTileTop(getMouseLocation().getY());

		g.setColor(Color.black);
		g.drawRect(x, y, ts, ts);

		final Placement placement = getGame().getBoard().getPlacementAt(getMouseLocation());

		if (placement != null)
		{
			double minDistance = 0;
			Section closestSection = null;

			for (final Section section : placement.getTile().getSections())
			{
				final Point point = getSectionPoint( section);
				final double distance = distance(this.mousePoint, point);

				if (closestSection == null || distance < minDistance)
				{
					closestSection = section;
					minDistance = distance;
				}
			}

			final Point point = getSectionPoint(closestSection);

			g.setColor(Color.black);
			g.drawOval(point.x - 4, point.y - 4, 8, 8);
			
			for (Section currentSection : getGame().getBoard().getFeatureOf(closestSection).getSections())
			{
				final Point currentPoint = getSectionPoint(currentSection);
				
				for (Section adjacentSection : getGame().getBoard().getAdjacentSections(currentSection))
				{
					final Point adjacentPoint = getSectionPoint(adjacentSection);
					
					g.drawLine(currentPoint.x, currentPoint.y, adjacentPoint.x, adjacentPoint.y);
				}
			}
		}
	}

	public double distance(final Point p1, final Point p2)
	{
		return Math.sqrt(0.0 + (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
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
		return getHeight() / 2 - ts / 2 + ts * y;
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

		final int tileSideW = getWidth() / 2 / (maxX - minX + 2);
		final int tileSideH = getHeight() / 2 / (maxY - minY + 2);
		return Math.min(tileSideH, tileSideW);
	}

	private Location getLocation(final int x, final int y)
	{
		final int ts = getTileSide();
		final int cx = getWidth() / 2 - ts / 2;
		final int cy = getHeight() / 2 - ts / 2;

		final Geometry geometry = (Geometry) getGame().getBoard().getGeometry();

		return new Location(geometry,// 
				(int) Math.floor((0.0 + x - cx) / ts),// 
				(int) Math.floor((0.0 + y - cy) / ts));
	}

	private LocalGuiClient getClient()
	{
		return this.localGuiClient;
	}

	private void setMouseLocation(final Location mouseLocation)
	{
		this.mouseLocation = mouseLocation;
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

			final Turn turn = new Turn(getClient().getPlayer(), //
					getClient().getCurrentTilePanel().getOrientedTile(), // 
					location, null, null);

			try
			{
				getClient().getServer().sendTurn(turn);
			}
			catch (final NotYourTurnException exception)
			{
				JOptionPane.showMessageDialog(this, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (final InvalidLocationException exception)
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
		this.mousePoint = event.getPoint();
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