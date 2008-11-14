package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.IClient;
import info.reflectionsofmind.connexion.IServer;
import info.reflectionsofmind.connexion.ServerException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
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

	private boolean turnMode = false;

	public LocalGuiClient(final IServer server)
	{
		super("Connexion");

		this.server = server;

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setMinimumSize(new Dimension(400, 400));
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(new MigLayout("", "[]6[grow]", "[]6[grow]"));

		this.currentTilePanel = new CurrentTilePanel(this);
		add(this.currentTilePanel, "grow");

		this.playerStatusPanel = new PlayerStatusPanel();
		add(this.playerStatusPanel, "wrap, grow");

		this.gameBoardPanel = new GameBoardPanel(this);
		add(this.gameBoardPanel, "span, grow");

		try
		{
			this.server.register(this);
		}
		catch (ServerException exception)
		{
			JOptionPane.showMessageDialog(null, "Cannot register on server.", "Error", JOptionPane.ERROR_MESSAGE);
		}
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
		this.turnMode = false;
	}

	public void setTurnMode()
	{
		this.turnMode = true;
	}

	public boolean isTurnMode()
	{
		return this.turnMode;
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

	public void updateInterface()
	{
		this.currentTilePanel.updateInterface();
		this.gameBoardPanel.repaint();

		if (getGame().getCurrentPlayer() == getPlayer())
		{
			setTurnMode();
		}
		else
		{
			setWaitingMode();
		}
	}

	public IServer getServer()
	{
		return this.server;
	}

	class PlayerStatusPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PlayerStatusPanel()
		{
			setBorder(BorderFactory.createLineBorder(Color.RED));
		}
	}
}
