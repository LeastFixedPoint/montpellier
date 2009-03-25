package info.reflectionsofmind.connexion.gui.play;

import info.reflectionsofmind.connexion.fortress.core.board.Board;
import info.reflectionsofmind.connexion.fortress.core.board.BoardUtil;
import info.reflectionsofmind.connexion.fortress.core.board.Feature;
import info.reflectionsofmind.connexion.fortress.core.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.game.Game;
import info.reflectionsofmind.connexion.fortress.core.game.GameUtil;
import info.reflectionsofmind.connexion.fortress.core.game.Player;
import info.reflectionsofmind.connexion.fortress.core.tile.Section;
import info.reflectionsofmind.connexion.fortress.core.tile.Type;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.ImmutableMap;

public class StatusBarPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final static Map<Type, String> FEATURE_NAMES = new ImmutableMap.Builder<Type, String>() //
			.put(Type.CASTLE, "A castle. ") //
			.put(Type.PASTURE, "A pasture. ") //
			.put(Type.CLOISTER, "A cloister. ") //
			.put(Type.ROAD, "A road. ") //
			.build();

	private final GameWindow clientUI;

	private final JLabel hintLabel;

	public StatusBarPanel(final GameWindow clientUI)
	{
		this.clientUI = clientUI;

		setLayout(new MigLayout("", "[]", "[]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.hintLabel = new JLabel("");
		add(this.hintLabel, "span, grow");
	}

	public GameWindow getClientUI()
	{
		return this.clientUI;
	}

	public void updateInterface()
	{
		final Section section = getClientUI().getGameBoardPanel().getSelectedSection();

		if (section == null)
		{
			this.hintLabel.setText("");
		}
		else
		{
			final Game game = getClientUI().getClient().getGame();
			final Board board = game.getBoard();
			final Feature feature = BoardUtil.getFeatureOf(board, section);

			final List<Meeple> meeplesOnFeature = BoardUtil.getMeeplesOnFeature(board, feature);
			final Map<Player, List<Meeple>> meeplesByPlayer = GameUtil.getMeeplesByPlayer(game, feature);

			final StringBuilder builder = new StringBuilder(FEATURE_NAMES.get(feature.getType()));

			if (feature.isCompleted())
			{
				builder.append("Completed, ").append(feature.getCompletedScore()).append(" points. ");
			}
			else
			{
				builder.append("Currently ").append(feature.getCurrentScore()).append(" points, ");
				builder.append("when completed ").append(feature.getCompletedScore()).append(" points. ");
			}

			if (meeplesOnFeature.size() > 0)
			{
				builder.append("Meeples: ");
				for (Player player : meeplesByPlayer.keySet())
				{
					if (player != meeplesByPlayer.keySet().iterator().next()) builder.append(", ");
					builder.append(player.getName()).append(" (").append(meeplesByPlayer.get(player).size()).append(")");
				}
			}

			this.hintLabel.setText(builder.toString());
		}
	}
}
