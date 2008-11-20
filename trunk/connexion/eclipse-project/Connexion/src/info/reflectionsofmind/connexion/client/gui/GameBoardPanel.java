package info.reflectionsofmind.connexion.client.gui;

import static java.awt.geom.AffineTransform.getQuadrantRotateInstance;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import info.reflectionsofmind.connexion.client.gui.ClientUI.State;
import info.reflectionsofmind.connexion.client.remote.RemoteServerException;
import info.reflectionsofmind.connexion.client.remote.ServerConnectionException;
import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.BoardUtil;
import info.reflectionsofmind.connexion.core.board.Feature;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.TilePlacement;
import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.tilelist.TileSourceUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
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

	private final ClientUI clientUI;
	private Point mousePoint = null;

	public GameBoardPanel(final ClientUI localGuiClient)
	{
		this.clientUI = localGuiClient;

		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	// ============================================================================================
	// === DRAWING METHODS
	// ============================================================================================

	@Override
	protected void paintComponent(final Graphics g)
	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		super.paintComponent(g);

		for (final TilePlacement placement : getClientUI().getClient().getGame().getBoard().getPlacements())
		{
			drawPlacement(g, placement);
		}

		final State turnMode = getClientUI().getTurnMode();

		if (turnMode == ClientUI.State.PLACE_TILE || turnMode == ClientUI.State.WAITING)
		{
			for (final TilePlacement placement : getClientUI().getClient().getGame().getBoard().getPlacements())
			{
				for (final IDirection direction : getClientUI().getClient().getGame().getBoard().getGeometry().getDirections())
				{
					final Location location = (Location) placement.getLocation().shift(direction);
					drawAllowedMarker(g, location);
				}
			}
		}

		drawMouseMarker(g);
	}

	private void drawPlacement(final Graphics g, final TilePlacement placement)
	{
		final ITileSource tileSource = getClientUI().getClient().getTileSource();
		final String code = placement.getTile().getCode();
		final TileData tileData = TileSourceUtil.getTileData(tileSource, code);
		final BufferedImage image = tileData.getImage();

		final int d = placement.getDirection().getIndex();
		final int w = image.getWidth();
		final int h = image.getHeight();

		final AffineTransform at = getQuadrantRotateInstance(d, w / 2, h / 2);
		at.concatenate(getTranslateInstance(0, h));
		at.concatenate(getScaleInstance(1, -1));

		final AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

		final Location location = (Location) placement.getLocation();

		final Point p = getLocationPoint(location);
		final int ls = getLocationSide();

		g.drawImage(op.filter(image, null), p.x, p.y, p.x + ls, p.y + ls, 0, 0, w, h, null);

		for (final Section section : placement.getTile().getSections())
		{
			final Point sectionPoint = getSectionPoint(section);

			g.drawRect(sectionPoint.x - 2, sectionPoint.y - 2, 4, 4);
		}
	}

	private void drawAllowedMarker(final Graphics g, final Location location)
	{
		final int ls = getLocationSide();
		final Point p = getLocationPoint(location);

		final OrientedTile orientedTile = getClientUI().getCurrentTilePanel().getOrientedTile();

		if (BoardUtil.isValidLocation(getClientUI().getClient().getGame().getBoard(), orientedTile, location))
		{
			g.setColor(Color.black);
			g.drawOval(p.x + ls / 4, p.y + ls / 4, ls / 2, ls / 2);
		}
	}

	private void drawMouseMarker(final Graphics g)
	{
		if (this.mousePoint == null) return;

		final Location mouseLocation = getLocationByPoint(this.mousePoint);
		final Board board = getClientUI().getClient().getGame().getBoard();

		if (getClientUI().getTurnMode() == ClientUI.State.PLACE_TILE)
		{
			final Point p = getLocationPoint(mouseLocation);
			final int ls = getLocationSide();

			final OrientedTile orientedTile = getClientUI().getCurrentTilePanel().getOrientedTile();

			if (BoardUtil.isValidLocation(board, orientedTile, mouseLocation))
			{
				g.setColor(Color.black);
				g.drawOval(p.x + 4 + ls / 4, p.y + 4 + ls / 4, ls / 2 - 8, ls / 2 - 8);
			}
		}

		final TilePlacement placement = BoardUtil.getPlacementAt(board, mouseLocation);

		if (placement != null)
		{
			final Section selectedSection = getSectionByPoint(this.mousePoint);
			final Point sp = getSectionPoint(selectedSection);

			g.setColor(Color.black);
			g.drawOval(sp.x - 8, sp.y - 8, 16, 16);

			highlightFeature(g, BoardUtil.getFeatureOf(board, selectedSection));
		}
	}

	private void highlightFeature(final Graphics g, final Feature feature)
	{
		g.setColor(Color.black);
		final Stroke oldStroke = ((Graphics2D) g).getStroke();
		((Graphics2D) g).setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		for (final Section currentSection : feature.getSections())
		{
			final Point currentPoint = getSectionPoint(currentSection);

			for (final Section adjacentSection : BoardUtil.getAdjacentSections(getClientUI().getClient().getGame().getBoard(), currentSection))
			{
				final Point adjacentPoint = getSectionPoint(adjacentSection);

				g.drawLine(currentPoint.x, currentPoint.y, adjacentPoint.x, adjacentPoint.y);
			}
		}

		((Graphics2D) g).setStroke(oldStroke);
	}

	// ============================================================================================
	// === ACTION METHODS
	// ============================================================================================

	private void placeTile(final Point clickPoint)
	{
		final Location location = getLocationByPoint(clickPoint);
		final OrientedTile orientedTile = getClientUI().getCurrentTilePanel().getOrientedTile();

		if (!BoardUtil.isValidLocation(getClientUI().getClient().getGame().getBoard(), orientedTile, location)) return;

		final Turn turn = new Turn(location, orientedTile.getDirection(), null, null);
		
		try
		{
			getClientUI().getClient().getServer().sendTurn(turn);
		}
		catch (final RemoteServerException exception)
		{
			JOptionPane.showMessageDialog(this, "Server refused turn.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (ServerConnectionException exception)
		{
			JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void placeMeeple(final Point clickPoint)
	{
		Meeple freeMeeple = null;

		for (final Meeple meeple : getClientUI().getClient().getPlayer().getMeeples())
		{
			if (getClientUI().getClient().getGame().getBoard().getMeepleSection(meeple) == null)
			{
				freeMeeple = meeple;
			}
		}

		if (freeMeeple == null)
		{
			JOptionPane.showMessageDialog(this, "You have no free meeples.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		final Section section = getSectionByPoint(clickPoint);

		try
		{
			getClientUI().getClient().getGame().getBoard().placeMeeple(freeMeeple, section);
		}
		catch (final FeatureTakenException exception)
		{
			JOptionPane.showMessageDialog(this, "This feature is already occupied.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// ============================================================================================
	// === UTILITY METHODS
	// ============================================================================================

	private Point getLocationPoint(final Location location)
	{
		final int ls = getLocationSide();
		final Point center = getCenter();
		return new Point(center.x + ls * location.getX(), center.y + ls * location.getY());
	}

	private Section getSectionByPoint(final Point point)
	{
		final TilePlacement placement = BoardUtil.getPlacementAt(getClientUI().getClient().getGame().getBoard(), getLocationByPoint(point));

		if (placement == null) return null;

		double minDistance = 0;
		Section closestSection = null;

		for (final Section section : placement.getTile().getSections())
		{
			final Point sectionPoint = getSectionPoint(section);
			final double distance = point.distance(sectionPoint);

			if (closestSection == null || distance < minDistance)
			{
				closestSection = section;
				minDistance = distance;
			}
		}

		return closestSection;
	}

	private Point getSectionPoint(final Section section)
	{
		final TilePlacement placement = BoardUtil.getPlacementOf(getClientUI().getClient().getGame().getBoard(), section.getTile());
		final Location location = (Location) placement.getLocation();

		final ITileSource tileSource = getClientUI().getClient().getTileSource();
		final String code = section.getTile().getCode();
		final TileData tileData = TileSourceUtil.getTileData(tileSource, code);
		final int sectionIndex = placement.getTile().getSections().indexOf(section);
		final Point2D point = tileData.getSectionPoint(sectionIndex);

		final Point p = getLocationPoint(location);
		final int ls = getLocationSide();

		int dx = (int) (ls * point.getX());
		int dy = (int) (ls * point.getY());

		for (int i = 0; i < placement.getDirection().getIndex(); i++)
		{
			final int tmp = dx;
			dx = dy;
			dy = ls - tmp;
		}

		dy = ls - dy;

		return new Point(p.x + dx, p.y + dy);
	}

	private Point getCenter()
	{
		final Board board = getClientUI().getClient().getGame().getBoard();
		final int tileSide = getLocationSide();

		final int cx = getWidth() / 2 - (BoardUtil.getMaxX(board) + BoardUtil.getMinX(board) + 1) * tileSide / 2;
		final int cy = getHeight() / 2 - (BoardUtil.getMaxY(board) + BoardUtil.getMinY(board) + 1) * tileSide / 2;

		return new Point(cx, cy);
	}

	private int getLocationSide()
	{
		final Board board = getClientUI().getClient().getGame().getBoard();

		final int tileSideW = getWidth() / (BoardUtil.getMaxX(board) - BoardUtil.getMinX(board) + 3);
		final int tileSideH = getHeight() / (BoardUtil.getMaxY(board) - BoardUtil.getMinY(board) + 3);

		return Math.min(tileSideH, tileSideW);
	}

	private Location getLocationByPoint(final Point point)
	{
		final int ls = getLocationSide();
		final Point cp = getCenter();

		final Geometry geometry = (Geometry) getClientUI().getClient().getGame().getBoard().getGeometry();

		return new Location(geometry,// 
				(int) Math.floor((0.0 + point.x - cp.x) / ls),// 
				(int) Math.floor((0.0 + point.y - cp.y) / ls));
	}

	// =============================================================================================
	// === MOUSE LISTENER
	// =============================================================================================

	private ClientUI getClientUI()
	{
		return this.clientUI;
	}

	@Override
	public void mouseClicked(final MouseEvent event)
	{
		if (getClientUI().getTurnMode() == ClientUI.State.PLACE_TILE) placeTile(event.getPoint());
		if (getClientUI().getTurnMode() == ClientUI.State.PLACE_MEEPLE) placeMeeple(event.getPoint());

		getClientUI().updateInterface();
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