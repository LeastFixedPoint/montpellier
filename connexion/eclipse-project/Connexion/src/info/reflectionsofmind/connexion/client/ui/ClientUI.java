package info.reflectionsofmind.connexion.client.ui;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.DisconnectReason;
import info.reflectionsofmind.connexion.server.IServer;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

/** GUI. Create only when the game has started on server. */
public class ClientUI extends JFrame
{
	public enum State
	{
		WAITING, TURN, DISCONNECTED, FINISHED
	};

	private static final long serialVersionUID = 1L;

	private final IServer server;
	private final Player player;
	private final IClient client;

	private final CurrentTilePanel currentTilePanel;
	private final InformationPanel informationPanel;
	private final GameBoardPanel gameBoardPanel;

	private State turnMode = State.WAITING;

	public ClientUI(final IServer server, final IClient client, final Player player)
	{
		super("Connexion " + player.getName() + " at " + server.getGame().getName());

		this.client = client;
		this.server = server;
		this.player = player;

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setMinimumSize(new Dimension(400, 400));
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(new MigLayout("", "[96]6[grow]", "[114]6[grow]"));

		this.currentTilePanel = new CurrentTilePanel(this);
		add(this.currentTilePanel, "grow");

		this.informationPanel = new InformationPanel(this);
		add(this.informationPanel, "wrap, grow");

		this.gameBoardPanel = new GameBoardPanel(this);
		add(this.gameBoardPanel, "span, grow");

		setVisible(true);
		
		updateInterface();
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public State getTurnMode()
	{
		return this.turnMode;
	}

	public void onTurn(final Turn turn)
	{
		if (getServer().getGame().getCurrentTile() == null)
		{
			this.turnMode = State.FINISHED;
		}
		
		this.currentTilePanel.reset();
		updateInterface();
	}

	public void onDisconnect(IClient client, DisconnectReason reason)
	{
		if (client == this.client)
		{
			if (reason != DisconnectReason.CLIENT_REQUEST)
			{
				JOptionPane.showMessageDialog(this, "You have been disconnected. Reason: " + reason, "Error", JOptionPane.ERROR_MESSAGE);
			}
	
			this.turnMode = State.DISCONNECTED;
		}
		else
		{
			JOptionPane.showMessageDialog(this, client.getName() + " have been disconnected. Reason: " + reason, "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		updateInterface();
	}

	public void updateInterface()
	{
		this.currentTilePanel.updateInterface();
		this.informationPanel.updateInterface();
		
		if (this.turnMode == State.TURN || this.turnMode == State.WAITING)
		{
			if (getServer().getGame().getCurrentPlayer() == getPlayer())
			{
				this.turnMode = State.TURN;
			}
			else
			{
				this.turnMode = State.WAITING;
			}
		}

		repaint();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		getServer().disconnect(this.client, DisconnectReason.CLIENT_REQUEST);
	}

	public IServer getServer()
	{
		return this.server;
	}
	
	public IClient getClient()
	{
		return this.client;
	}

	public CurrentTilePanel getCurrentTilePanel()
	{
		return this.currentTilePanel;
	}

	public GameBoardPanel getGameBoardPanel()
	{
		return this.gameBoardPanel;
	}
}
