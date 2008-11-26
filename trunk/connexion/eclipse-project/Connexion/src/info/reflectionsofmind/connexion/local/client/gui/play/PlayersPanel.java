package info.reflectionsofmind.connexion.local.client.gui.play;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.util.Colors;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.ImmutableMap;

class PlayersPanel extends JPanel
{
	private final static Map<Meeple.Type, String> MEEPLE_NAMES = new ImmutableMap.Builder<Meeple.Type, String>() //
			.put(Meeple.Type.MEEPLE, "Meeples: ") //
			.build();

	private final ClientUI clientUI;
	private final Map<Player, PlayerStatusPanel> panels = new HashMap<Player, PlayerStatusPanel>();

	public PlayersPanel(final ClientUI clientUI)
	{
		this.clientUI = clientUI;
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		final Game game = this.clientUI.getClient().getGame();

		for (final Player player : game.getPlayers())
		{
			final PlayerStatusPanel panel = new PlayerStatusPanel(player);
			panel.setBorder(BorderFactory.createLineBorder(Colors.getColor(game.getPlayers().indexOf(player)), 3));
			this.panels.put(player, panel);
			add(panel, "span, grow");
		}
	}

	public void updateInterface()
	{
		for (final Player player : this.clientUI.getClient().getGame().getPlayers())
		{
			this.panels.get(player).updateInterface();
		}
	}

	public ClientUI getClientUI()
	{
		return this.clientUI;
	}

	class PlayerStatusPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private final JLabel turnMarker;
		private final Player player;
		private final JLabel scoreLabel;
		private final JLabel meeplesLabel;

		public PlayerStatusPanel(final Player player)
		{
			this.player = player;

			setLayout(new MigLayout("ins 0", "[30]6[120]6[60]6[grow]", "[center]"));

			final JLabel nameLabel = new JLabel(player.getName());

			if (PlayersPanel.this.clientUI.getClient().getPlayer() == player)
			{
				nameLabel.setText(nameLabel.getText() + " (You)");
			}

			final int playerIndex = getClientUI().getClient().getGame().getPlayers().indexOf(player);

			final BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = image.createGraphics();
			g.setColor(Colors.getColor(playerIndex));
			g.setBackground(Colors.getColor(playerIndex));
			g.fillRect(0, 0, 16, 16);

			this.turnMarker = new JLabel(new ImageIcon(image), SwingConstants.CENTER);

			this.scoreLabel = new JLabel("" + player.getScore());
			this.scoreLabel.setFont(getFont().deriveFont(20.0f).deriveFont(Font.BOLD));
			this.meeplesLabel = new JLabel("Meeplz");

			add(this.turnMarker, "grow");
			add(nameLabel, "grow");
			add(this.scoreLabel, "grow");
			add(this.meeplesLabel, "grow");
		}

		public void updateInterface()
		{
			this.turnMarker.setVisible(this.player == getClientUI().getClient().getGame().getCurrentPlayer());
			this.scoreLabel.setText("" + this.player.getScore());
			this.meeplesLabel.setText(MEEPLE_NAMES.get(Meeple.Type.MEEPLE) + player.getMeeples().size());
		}
	}
}