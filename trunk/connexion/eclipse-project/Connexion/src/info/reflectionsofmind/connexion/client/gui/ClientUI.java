package info.reflectionsofmind.connexion.client.gui;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.local.DesynchronizationException;
import info.reflectionsofmind.connexion.client.remote.RemoteServerException;
import info.reflectionsofmind.connexion.client.remote.ServerConnectionException;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.FeatureTakenException;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Section;

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
	private final GameInfoPanel gameInfoPanel;

	private State turnMode = State.WAITING;
	private Turn turn = null;

	public ClientUI(final IClient client)
	{
		super("Connexion :: Client :: " + client.getGame().getName() + " :: " + client.getPlayer().getName());

		this.client = client;

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setMinimumSize(new Dimension(400, 400));
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(new MigLayout("", "[]6[]6[grow]", "[114]6[grow]"));

		this.currentTilePanel = new CurrentTilePanel(this);
		add(this.currentTilePanel, "grow");

		this.gameInfoPanel = new GameInfoPanel(this);
		add(this.gameInfoPanel, "grow");

		this.informationPanel = new InformationPanel(this);
		add(this.informationPanel, "wrap, grow");

		this.gameBoardPanel = new GameBoardPanel(this);
		add(this.gameBoardPanel, "span, grow");

		if (getClient().getGame().getCurrentPlayer() == getClient().getPlayer())
		{
			this.turnMode = State.PLACE_TILE;
			this.turn = new Turn();
		}

		setVisible(true);

		updateInterface();
	}

	public void placeTile(ILocation location, IDirection direction)
	{
		if (getTurnMode() != State.PLACE_TILE) JOptionPane.showMessageDialog(this, "Cannot place tile now.", "Error", JOptionPane.ERROR_MESSAGE);

		try
		{
			getClient().getGame().getBoard().placeTile(getClient().getGame().getCurrentTile(), location, direction);
		}
		catch (InvalidTileLocationException exception)
		{
			JOptionPane.showMessageDialog(this, "Invalid tile location.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.turn.addTilePlacement(location, direction);
		this.turnMode = State.PLACE_MEEPLE;
	}

	public void placeMeeple(Meeple meeple, Section section)
	{
		if (getTurnMode() != State.PLACE_MEEPLE) JOptionPane.showMessageDialog(this, "Cannot place meeple now.", "Error", JOptionPane.ERROR_MESSAGE);

		try
		{
			getClient().getGame().getBoard().placeMeeple(meeple, section);
		}
		catch (FeatureTakenException exception)
		{
			JOptionPane.showMessageDialog(this, "This feature is occupied.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.turn.addMeeplePlacement(meeple, section);
		endTurn();
	}

	public void endTurn()
	{
		try
		{
			getClient().getGame().endTurn();
			this.turnMode = State.WAITING;
			getClient().getServer().sendTurn(this.turn);
		}
		catch (final RemoteServerException exception)
		{
			JOptionPane.showMessageDialog(this, "Server refused turn.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (final ServerConnectionException exception)
		{
			JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		updateInterface();
	}

	public State getTurnMode()
	{
		return this.turnMode;
	}

	public void onTurn()
	{
		if (getClient().getGame().isFinished())
		{
			this.turnMode = State.FINISHED;
		}
		else if (getClient().getGame().getCurrentPlayer() == getClient().getPlayer())
		{
			this.turnMode = State.PLACE_TILE;
			this.turn = new Turn();
		}

		this.currentTilePanel.reset();
		updateInterface();
	}

	public void updateInterface()
	{
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

		this.currentTilePanel.updateInterface();
		this.informationPanel.updateInterface();
		this.gameInfoPanel.updateInterface();

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

	public Turn getTurn()
	{
		return this.turn;
	}

	public void onDesync(final DesynchronizationException desynhronizationcException)
	{
		JOptionPane.showMessageDialog(this, "Desynchronization occured, game cannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
		this.turnMode = State.ERROR;
	}
}
