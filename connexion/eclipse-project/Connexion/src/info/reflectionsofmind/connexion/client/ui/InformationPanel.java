package info.reflectionsofmind.connexion.client.ui;

import info.reflectionsofmind.connexion.client.ui.ClientUI.State;
import info.reflectionsofmind.connexion.core.game.Player;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

class InformationPanel extends JPanel
{
	private final ClientUI clientUI;
	private static final long serialVersionUID = 1L;
	private final Map<Player, PlayerStatusPanel> panels = new HashMap<Player, PlayerStatusPanel>();

	public InformationPanel(ClientUI clientUI)
	{
		this.clientUI = clientUI;
		setLayout(new MigLayout("", "[180]6[180]6[180]6[180]6[180]", "[]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		for (final Player player : this.clientUI.getServer().getGame().getPlayers())
		{
			final PlayerStatusPanel panel = new PlayerStatusPanel(player);
			this.panels.put(player, panel);
			add(panel, "grow");
		}
	}

	public void updateInterface()
	{
		for (Player player : this.clientUI.getServer().getGame().getPlayers())
		{
			this.panels.get(player).setBackground(Color.WHITE);
		}

		if (this.clientUI.getTurnMode() == State.TURN || this.clientUI.getTurnMode() == State.WAITING)
		{
			final Player currentPlayer = this.clientUI.getServer().getGame().getCurrentPlayer();
			this.panels.get(currentPlayer).setBackground(Color.LIGHT_GRAY);
		}
	}

	class PlayerStatusPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PlayerStatusPanel(final Player player)
		{
			setLayout(new MigLayout("ins 0", "[180]", "[18]6[max]6[18]"));

			final JLabel nameLabel = new JLabel(player.getName());
			
			if (clientUI.getPlayer() == player)
			{
				nameLabel.setForeground(Color.RED);
				nameLabel.setText(nameLabel.getText() + " (You)");
			}
			
			final JLabel scoreLabel = new JLabel("" + player.getScore());
			scoreLabel.setFont(getFont().deriveFont(48.0f));
			final JLabel meeplesPanel = new JLabel("Meeplz");

			add(nameLabel, "span, al 50%");
			add(scoreLabel, "span, al 50%");
			add(meeplesPanel, "span, al 50%");
		}
	}
}