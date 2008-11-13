package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class LocalGuiClient extends JFrame implements IClient
{
	private static final long serialVersionUID = 1L;

	private final IServer server;

	private Player player;
	private Game game;

	private final CurrentTilePanel currentTilePanel;
	private final PlayerStatusPanel playerStatusPanel;
	private final GameBoardPanel gameBoardPanel;

	public LocalGuiClient(final IServer server)
	{
		super("Connexion");

		this.server = server;

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setMinimumSize(new Dimension(400, 400));
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(new MigLayout("", "[100]6[grow]", "[100]6[grow]"));

		this.currentTilePanel = new CurrentTilePanel();
		add(this.currentTilePanel, "grow");

		this.playerStatusPanel = new PlayerStatusPanel();
		add(this.playerStatusPanel, "wrap, grow");

		this.gameBoardPanel = new GameBoardPanel();
		add(this.gameBoardPanel, "span, grow");

		this.server.register(this);
	}

	@Override
	public String getName()
	{
		return "Alpha";
	}

	@Override
	public void onStart(final Game game, final Player player)
	{
		this.game = game;
		this.player = player;
		setVisible(true);

		updateInterface();
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Game getGame()
	{
		return this.game;
	}

	public void setWaitingMode()
	{
		// TODO Actions in waiting mode
	}

	public void setTurnMode()
	{
		// TODO Actions in turn mode
	}

	public void onTurn(final Turn turn)
	{
		this.currentTilePanel.updateInterface();
		
		try
		{
			getGame().doTurn(turn);
		}
		catch (final NotYourTurnException exception)
		{
			JOptionPane.showMessageDialog(this, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
		}

		updateInterface();
	}

	private void updateInterface()
	{
		this.currentTilePanel.updateInterface();

		if (getGame().getCurrentPlayer() == getPlayer())
		{
			setTurnMode();
		}
		else
		{
			setWaitingMode();
		}
	}

	class CurrentTilePanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private final StretchingImage tileImage;

		public CurrentTilePanel()
		{
			setBorder(BorderFactory.createLineBorder(Color.RED));
			setLayout(new MigLayout("", "[max]", "[max]"));

			this.tileImage = new StretchingImage(getIcon());
			add(this.tileImage, "grow");
		}

		public void updateInterface()
		{
			if (this.tileImage != null)
			{
				this.tileImage.setIcon(getIcon());
			}
		}

		private ImageIcon getIcon()
		{
			if (getGame() != null)
			{
				return new ImageIcon(getGame().getTileImageURL(getGame().getNextTile()));
			}
			else
			{
				return new ImageIcon();
			}
		}
	}

	class PlayerStatusPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PlayerStatusPanel()
		{
			setBorder(BorderFactory.createLineBorder(Color.RED));
		}
	}

	class GameBoardPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public GameBoardPanel()
		{
			setBorder(BorderFactory.createLineBorder(Color.RED));
		}
	}
}
