package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.ServerException;
import info.reflectionsofmind.connexion.core.board.Placement;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GameBoardPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final LocalGuiClient localGuiClient;
	private Location locationUnderMouse = null;
	
	public GameBoardPanel(LocalGuiClient localGuiClient)
	{
		this.localGuiClient = localGuiClient;
		setBorder(BorderFactory.createLineBorder(Color.RED));
		
		final GameBoardMouseListener listener = new GameBoardMouseListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	private void drawPlacement(Graphics g, Placement placement)
	{
		final int ts = getTileSide();
		final int cx = (getWidth() / 2 - ts / 2);
		final int cy = (getHeight() / 2 - ts / 2);

		final Image image = new ImageIcon(this.localGuiClient.getGame().getTileImageURL(placement.getTile())).getImage();
		final Location location = (Location) placement.getLocation();

		final int dx1 = cx + location.getX() * ts;
		final int dx2 = dx1 + ts;

		final int dy1 = cy - location.getY() * ts;
		final int dy2 = dy1 + ts;

		final int iw = image.getWidth(null);
		final int ih = image.getHeight(null);

		g.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, iw, ih, null);
	}

	private void drawAllowedMarker(Graphics g, Location location)
	{
		for (IDirection direction : this.localGuiClient.getGame().getBoard().getGeometry().getDirections())
		{
			final int ts = getTileSide();
			final int cx = (getWidth() / 2 - ts / 2);
			final int cy = (getHeight() / 2 - ts / 2);

			final int x = cx + location.getX() * ts + ts / 4;
			final int w = ts / 2;

			final int y = cy - location.getY() * ts + ts / 4;
			final int h = ts / 2;

			if (this.localGuiClient.getGame().getBoard().isValidLocation(this.localGuiClient.getGame().getCurrentTile(), location, direction))
			{
				g.drawOval(x, y, w, h);
			}

			g.drawString(location.toString(), x, y);
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for (Placement placement : this.localGuiClient.getGame().getBoard().getPlacements())
		{
			drawPlacement(g, placement);
		}

		for (Placement placement : this.localGuiClient.getGame().getBoard().getPlacements())
		{
			for (IDirection direction : this.localGuiClient.getGame().getBoard().getGeometry().getDirections())
			{
				final Location location = (Location) placement.getLocation().shift(direction);
				drawAllowedMarker(g, location);
			}
		}
		
		drawMouseMarker(g);
	}

	private void drawMouseMarker(Graphics g)
	{
		if (locationUnderMouse == null) return;

		final int ts = getTileSide();
		final int cx = (getWidth() / 2 - ts / 2);
		final int cy = (getHeight() / 2 - ts / 2);

		final int x = cx + locationUnderMouse.getX() * ts;
		final int w = ts;

		final int y = cy - locationUnderMouse.getY() * ts;
		final int h = ts;
		
		g.drawRect(x, y, w, h);
	}

	int getTileSide()
	{
		int minX = 0, maxX = 0, minY = 0, maxY = 0;

		for (Placement placement : this.localGuiClient.getGame().getBoard().getPlacements())
		{
			final Location location = (Location) placement.getLocation();
			minX = Math.min(location.getX(), minX);
			maxX = Math.max(location.getX(), maxX);
			minY = Math.min(location.getY(), minY);
			maxY = Math.max(location.getY(), maxY);
		}

		int tileSideW = (getWidth() / 4) / (maxX - minX + 1);
		int tileSideH = (getHeight() / 4) / (maxY - minY + 1);
		return Math.min(tileSideH, tileSideW);
	}

	class GameBoardMouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			if (localGuiClient.isTurnMode())
			{
				final Location location = getLocation(event.getX(), event.getY());

				final Turn turn = new Turn(localGuiClient.getPlayer(), localGuiClient.getGame().getCurrentTile(), // 
						location, localGuiClient.getGame().getBoard().getGeometry().getNthDirection(0), // 
						null, null);

				try
				{
					localGuiClient.getServer().sendTurn(turn);
				}
				catch (ServerException exception)
				{
					exception.printStackTrace();
					JOptionPane.showMessageDialog(localGuiClient, "Server error.", "Error", JOptionPane.ERROR_MESSAGE);
				}

				localGuiClient.updateInterface();
			}
			else
			{
				JOptionPane.showMessageDialog(localGuiClient, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent event)
		{
			locationUnderMouse = getLocation(event.getX(), event.getY());
			GameBoardPanel.this.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			locationUnderMouse = null;
			GameBoardPanel.this.repaint();
		}

		private Location getLocation(int x, int y)
		{
			int ts = getTileSide();
			final int cx = (getWidth() / 2 - ts / 2);
			final int cy = (getHeight() / 2 - ts / 2);

			final Geometry geometry = (Geometry) localGuiClient.getGame().getBoard().getGeometry();

			return new Location(geometry,// 
					-1 - (int) Math.floor((0.0 + cx - x) / ts),// 
					+1 + (int) Math.floor((0.0 + cy - y) / ts));
		}
	}
}