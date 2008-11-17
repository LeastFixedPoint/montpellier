package info.reflectionsofmind.connexion.ui;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.TilePlacement;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.tilelist.TileSourceUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
	private Point mousePoint = null;

	public GameBoardPanel(final LocalGuiClient localGuiClient)
	{
		this.localGuiClient = localGuiClient;
		setBorder(BorderFactory.createLineBorder(Color.RED));

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private void drawPlacement(final Graphics g, final TilePlacement placement)
	{
		final TileData tileData = TileSourceUtil.getTileData(this.localGuiClient.getServer().getTileSource(), placement.getTile());
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
		final TilePlacement placement = this.localGuiClient.getServer().getGame().getBoard().getPlacementOf(section.getTile());
		final Location location = (Location) placement.getLocation();
		final TileData tileData = TileSourceUtil.getTileData(this.localGuiClient.getServer().getTileSource(), section.getTile());
		final Point2D point = tileData.getSectionPoint(section);

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
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final int ts = getTileSide();
		final int x = getTileLeft(location.getX()) + ts / 4;
		final int y = getTileTop(location.getY()) + ts / 4;

		final OrientedTile orientedTile = this.localGuiClient.getCurrentTilePanel().getOrientedTile();

		if (this.localGuiClient.getServer().getGame().getBoard().isValidLocation(orientedTile, location))
		{
			g.setColor(Color.black);
			g.drawOval(x, y, ts / 2, ts / 2);
		}
	}

	private void drawMouseMarker(final Graphics g)
	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.mousePoint == null) return;

		final Location mouseLocation = getBoardLocation(this.mousePoint);
		final int ts = getTileSide();
		final int x = getTileLeft(mouseLocation.getX()) + ts / 4;
		final int y = getTileTop(mouseLocation.getY()) + ts / 4;

		final OrientedTile orientedTile = this.localGuiClient.getCurrentTilePanel().getOrientedTile();

		if (this.localGuiClient.getServer().getGame().getBoard().isValidLocation(orientedTile, mouseLocation))
		{
			g.setColor(Color.black);
			g.drawOval(x + 4, y + 4, ts / 2 - 8, ts / 2 - 8);
		}

		final TilePlacement placement = this.localGuiClient.getServer().getGame().getBoard().getPlacementAt(mouseLocation);

		if (placement != null)
		{
			double minDistance = 0;
			Section closestSection = null;

			for (final Section section : placement.getTile().getSections())
			{
				final Point point = getSectionPoint(section);
				final double distance = this.mousePoint.distance(point);
				
				if (closestSection == null || distance < minDistance)
				{
					closestSection = section;
					minDistance = distance;
				}
			}

			final Point point = getSectionPoint(closestSection);

			g.setColor(Color.black);
			g.drawOval(point.x - 4, point.y - 4, 8, 8);

			for (final Section currentSection : this.localGuiClient.getServer().getGame().getBoard().getFeatureOf(closestSection).getSections())
			{
				final Point currentPoint = getSectionPoint(currentSection);

				for (final Section adjacentSection : this.localGuiClient.getServer().getGame().getBoard().getAdjacentSections(currentSection))
				{
					final Point adjacentPoint = getSectionPoint(adjacentSection);

					g.drawLine(currentPoint.x, currentPoint.y, adjacentPoint.x, adjacentPoint.y);
				}
			}
		}
	}

	@Override
	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		for (final TilePlacement placement : this.localGuiClient.getServer().getGame().getBoard().getPlacements())
		{
			drawPlacement(g, placement);
		}

		for (final TilePlacement placement : this.localGuiClient.getServer().getGame().getBoard().getPlacements())
		{
			for (final IDirection direction : this.localGuiClient.getServer().getGame().getBoard().getGeometry().getDirections())
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

		for (final TilePlacement placement : this.localGuiClient.getServer().getGame().getBoard().getPlacements())
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

	private Location getBoardLocation(final Point point)
	{
		final int ts = getTileSide();
		final int cx = getWidth() / 2 - ts / 2;
		final int cy = getHeight() / 2 - ts / 2;

		final Geometry geometry = (Geometry) this.localGuiClient.getServer().getGame().getBoard().getGeometry();

		return new Location(geometry,// 
				(int) Math.floor((0.0 + point.x - cx) / ts),// 
				(int) Math.floor((0.0 + point.y - cy) / ts));
	}

	// =============================================================================================
	// === MOUSE LISTENER
	// =============================================================================================

	@Override
	public void mouseClicked(final MouseEvent event)
	{
		if (this.localGuiClient.isTurnMode())
		{
			final Location location = getBoardLocation(event.getPoint());

			final Turn turn = new Turn(this.localGuiClient.getPlayer(), //
					this.localGuiClient.getCurrentTilePanel().getOrientedTile(), // 
					location, null, null);

			try
			{
				this.localGuiClient.getServer().sendTurn(turn);
			}
			catch (final NotYourTurnException exception)
			{
				JOptionPane.showMessageDialog(this, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (final InvalidTileLocationException exception)
			{
				JOptionPane.showMessageDialog(this, "Invalid location!", "Error", JOptionPane.ERROR_MESSAGE);
			}

			this.localGuiClient.updateInterface();
		}
		else
		{
			JOptionPane.showMessageDialog(this.localGuiClient, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseMoved(final MouseEvent event)
	{
		this.mousePoint = event.getPoint();
		repaint();
	}

	@Override
	public void mouseExited(final MouseEvent e)
	{
		this.mousePoint = null;
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