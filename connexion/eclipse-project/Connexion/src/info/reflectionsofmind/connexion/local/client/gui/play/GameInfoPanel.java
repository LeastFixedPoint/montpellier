package info.reflectionsofmind.connexion.local.client.gui.play;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class GameInfoPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final ClientUI clientUI;

	private JLabel tileCountLabel;
	private JLabel tileCountDescriptionLabel;
	private JLabel gameTimeLabel;
	private JLabel turnTimeLabel;

	private Integer totalTiles;
	private final long gameStartTimestamp;
	private final long turnStartTimestamp;

	public GameInfoPanel(final ClientUI clientUI)
	{
		this.clientUI = clientUI;

		setLayout(new MigLayout("ins 0", "[180]", "[grow]6[grow]6[grow]"));

		add(createTileCountPanel(), "grow, span");
		add(createGameTimePanel(), "grow, span");
		add(createTurnTimePanel(), "grow, span");

		this.gameStartTimestamp = System.currentTimeMillis();
		this.turnStartTimestamp = System.currentTimeMillis();

		new TimerThread().start();
	}

	private JPanel createTileCountPanel()
	{
		this.totalTiles = getClientUI().getClient().getGame().getSequence().getTotalNumberOfTiles();

		final String title = this.totalTiles == null ? "Tiles placed" : "Tiles left";

		final JPanel panel = new JPanel(new MigLayout("", "[grow]", "[][]"));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		this.tileCountDescriptionLabel = new JLabel(title, SwingConstants.CENTER);

		this.tileCountLabel = new JLabel("??? / ???", SwingConstants.CENTER);
		this.tileCountLabel.setFont(this.tileCountLabel.getFont().deriveFont(24.0f).deriveFont(Font.BOLD));

		panel.add(this.tileCountDescriptionLabel, "span, grow");
		panel.add(this.tileCountLabel, "span, grow");

		return panel;
	}

	private JPanel createGameTimePanel()
	{
		final JPanel panel = new JPanel(new MigLayout("", "[grow]", "[]"));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		this.gameTimeLabel = new JLabel("00:00", SwingConstants.CENTER);
		this.gameTimeLabel.setFont(this.gameTimeLabel.getFont().deriveFont(24.0f).deriveFont(Font.BOLD));

		panel.add(new JLabel("Game time", SwingConstants.CENTER), "span, grow");
		panel.add(this.gameTimeLabel, "span, grow");

		return panel;
	}

	private JPanel createTurnTimePanel()
	{
		final JPanel panel = new JPanel(new MigLayout("", "[grow]", "[]"));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		this.turnTimeLabel = new JLabel("00:00", SwingConstants.CENTER);
		this.turnTimeLabel.setFont(this.turnTimeLabel.getFont().deriveFont(24.0f).deriveFont(Font.BOLD));

		panel.add(new JLabel("Turn time", SwingConstants.CENTER), "span, grow");
		panel.add(this.turnTimeLabel, "span, grow");

		return panel;
	}

	public ClientUI getClientUI()
	{
		return this.clientUI;
	}

	public void updateInterface()
	{
		final Integer current = getClientUI().getClient().getGame().getBoard().getPlacements().size();
		final Integer total = getClientUI().getClient().getGame().getSequence().getTotalNumberOfTiles();

		if (total != null)
		{
			this.tileCountLabel.setText(total - current + " of " + total);
		}
		else
		{
			this.tileCountLabel.setText("" + current);
		}
	}

	private void updateTime()
	{
		final long currentTimestamp = System.currentTimeMillis();

		final long gameSeconds = (currentTimestamp - this.gameStartTimestamp) / 1000;
		final long turnSeconds = (currentTimestamp - this.turnStartTimestamp) / 1000;
		
		this.gameTimeLabel.setText(String.format("%02d:%02d", (gameSeconds / 60), (gameSeconds % 60)));
		this.turnTimeLabel.setText(String.format("%02d:%02d", (turnSeconds / 60), (turnSeconds % 60)));
	}

	private class TimerThread extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(100);

					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							updateTime();
						}
					});
				}
				catch (final InterruptedException exception)
				{
				}

				final ClientUI.State turnMode = getClientUI().getTurnMode();

				if (turnMode != ClientUI.State.PLACE_TILE //
						&& turnMode != ClientUI.State.PLACE_MEEPLE //
						&& turnMode != ClientUI.State.WAITING) break;
			}
		}
	}
}
