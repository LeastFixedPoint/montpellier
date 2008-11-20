package info.reflectionsofmind.connexion.client.gui;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.local.DesynhronizationException;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

/** GUI. Create only when the game has started on server. */
public class ClientUI extends JFrame
{
	public enum State
	{
		WAITING, PLACE_TILE, PLACE_MEEPLE, DISCONNECTED, FINISHED, ERROR
	};

	private static final long serialVersionUID = 1L;

	private final IClient client;

	private final CurrentTilePanel currentTilePanel;
	private final InformationPanel informationPanel;
	private final GameBoardPanel gameBoardPanel;

	private State turnMode = State.WAITING;

	public ClientUI(final IClient client)
	{
		super("Connexion :: Client :: " + client.getGame().getName() + " :: "+ client.getPlayer().getName());

		this.client = client;

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

	public State getTurnMode()
	{
		return this.turnMode;
	}

	public void onTurn(final ServerTurnEvent event)
	{
		if (getClient().getGame().getCurrentTile() == null)
		{
			this.turnMode = State.FINISHED;
		}
		
		this.currentTilePanel.reset();
		updateInterface();
	}

	public void updateInterface()
	{
		this.currentTilePanel.updateInterface();
		this.informationPanel.updateInterface();
		
		if (this.turnMode == State.PLACE_TILE || this.turnMode == State.WAITING)
		{
			if (getClient().getGame().getCurrentPlayer() == getClient().getPlayer())
			{
				this.turnMode = State.PLACE_TILE;
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

	public void onDesync(DesynhronizationException desynhronizationcException)
	{
		JOptionPane.showMessageDialog(this, "Desynchronization occured, game cannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
		this.turnMode = State.ERROR;
	}
}
