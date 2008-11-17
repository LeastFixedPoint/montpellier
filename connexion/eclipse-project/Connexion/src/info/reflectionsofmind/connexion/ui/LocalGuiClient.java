package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.IClient;
import info.reflectionsofmind.connexion.IServer;
import info.reflectionsofmind.connexion.ServerException;
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
		catch (final ServerException exception)
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
	public void onStart(final Player player)
	{
		this.player = player;
		setVisible(true);

		updateInterface();
	}
	
	@Override
	public void onEnd()
	{
		JOptionPane.showMessageDialog(this, "Game Over", "Connexion", JOptionPane.INFORMATION_MESSAGE);
	}

	public Player getPlayer()
	{
		return this.player;
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
		this.currentTilePanel.reset();
		updateInterface();
	}

	public void updateInterface()
	{
		this.currentTilePanel.updateInterface();

		if (getServer().getGame() != null)
		{
			if (getServer().getGame().getCurrentPlayer() == getPlayer())
			{
				setTurnMode();
			}
			else
			{
				setWaitingMode();
			}
		}
		
		repaint();
	}

	public IServer getServer()
	{
		return this.server;
	}
	
	public CurrentTilePanel getCurrentTilePanel()
	{
		return this.currentTilePanel;
	}
	
	public GameBoardPanel getGameBoardPanel()
	{
		return this.gameBoardPanel;
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
