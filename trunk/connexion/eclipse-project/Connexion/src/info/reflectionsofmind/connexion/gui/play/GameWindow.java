package info.reflectionsofmind.connexion.gui.play;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.exception.MeeplePlacementException;
import info.reflectionsofmind.connexion.core.board.exception.TilePlacementException;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.GameUtil;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.platform.client.IClient;
import info.reflectionsofmind.connexion.platform.client.exception.DesynchronizationException;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.common.Participant;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

/** GUI. Create only when the game has started on server. */
public class GameWindow extends JFrame implements IClient.IListener
{
	public enum State
	{
		WAITING, PLACE_TILE, PLACE_MEEPLE, DISCONNECTED, FINISHED, ERROR, CLOSED
	};

	private static final long serialVersionUID = 1L;

	private final IClient client;

	private final CurrentTilePanel currentTilePanel;
	private final PlayersPanel playersPanel;
	private final GameBoardPanel gameBoardPanel;
	private final GameInfoPanel gameInfoPanel;
	private final StatusBarPanel statusBarPanel;

	private State turnMode = State.WAITING;
	private Turn turn = null;

	public GameWindow(final IClient client)
	{
		super("Connexion :: Playing game :: " + client.getName());

		this.client = client;
		this.client.addListener(this);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(700, 500);
		setMinimumSize(new Dimension(700, 500));
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(new MigLayout("", "[]6[]6[grow]", "[180]6[grow]6[30]"));

		this.currentTilePanel = new CurrentTilePanel(this);
		add(this.currentTilePanel, "grow");

		this.gameInfoPanel = new GameInfoPanel(this);
		add(this.gameInfoPanel, "grow");

		this.playersPanel = new PlayersPanel(this);
		add(this.playersPanel, "grow, wrap");

		this.gameBoardPanel = new GameBoardPanel(this);
		add(this.gameBoardPanel, "grow, span");

		this.statusBarPanel = new StatusBarPanel(this);
		add(this.statusBarPanel, "grow, span");

		if (isMyTurn())
		{
			this.turnMode = State.PLACE_TILE;
			this.turn = new Turn();
		}

		setVisible(true);

		updateInterface();
	}

	// ============================================================================================
	// === CLIENT LISTENER
	// ============================================================================================

	@Override
	public void onTurn(Turn turn, String nextTileCode)
	{
		if (getClient().getGame().isFinished())
		{
			this.turnMode = State.FINISHED;
		}
		else if (isMyTurn())
		{
			this.turnMode = State.PLACE_TILE;
			this.turn = new Turn();
		}

		this.currentTilePanel.reset();
		updateInterface();
	}

	@Override
	public void onConnectionAccepted()
	{
	}

	@Override
	public void onAfterConnectionBroken(DisconnectReason reason)
	{
	}

	@Override
	public void onChatMessage(Participant sender, String message)
	{
	}

	@Override
	public void onClientConnected(Participant client)
	{
	}

	@Override
	public void onClientDisconnected(Participant client)
	{
	}

	@Override
	public void onStart()
	{
	}

	// ============================================================================================
	// === STUFF
	// ============================================================================================

	private boolean isMyTurn()
	{
		final int playerIndex = getClient().getGame().getPlayers().indexOf(getClient().getGame().getCurrentPlayer());
		final int clientIndex = getClient().getParticipants().indexOf(getClient().getParticipant());

		return playerIndex == clientIndex;
	}

	public void placeTile(final ILocation location, final IDirection direction)
	{
		if (getTurnMode() != State.PLACE_TILE) JOptionPane.showMessageDialog(this, "Cannot place tile now.", "Error", JOptionPane.ERROR_MESSAGE);

		try
		{
			getClient().getGame().getBoard().placeTile(getClient().getGame().getCurrentTile(), location, direction);
		}
		catch (TilePlacementException exception)
		{
			JOptionPane.showMessageDialog(this, "Invalid tile location.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.turn.addTilePlacement(location, direction);
		this.turnMode = State.PLACE_MEEPLE;
	}

	public void placeMeeple(final Meeple.Type meepleType, final Section section)
	{
		if (getTurnMode() != State.PLACE_MEEPLE) JOptionPane.showMessageDialog(this, "Cannot place meeple now.", "Error", JOptionPane.ERROR_MESSAGE);

		final Game game = getClient().getGame();

		final Meeple freeMeeple = GameUtil.getFreeMeepleOfType(game.getBoard(), game.getCurrentPlayer(), meepleType);

		try
		{
			game.getBoard().placeMeeple(freeMeeple, section);
		}
		catch (final MeeplePlacementException exception)
		{
			JOptionPane.showMessageDialog(this, exception.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.turn.addMeeplePlacement(meepleType, game.getCurrentTile().getSections().indexOf(section));
		endTurn();
	}

	public void endTurn()
	{
		this.turnMode = State.WAITING;
		getClient().sendLastTurn();
		updateInterface();
	}

	public State getTurnMode()
	{
		return this.turnMode;
	}

	public void updateInterface()
	{
		if (this.turnMode == State.PLACE_TILE || this.turnMode == State.WAITING)
		{
			if (isMyTurn())
			{
				this.turnMode = State.PLACE_TILE;
			}
			else
			{
				this.turnMode = State.WAITING;
			}
		}

		this.currentTilePanel.updateInterface();
		this.playersPanel.updateInterface();
		this.gameInfoPanel.updateInterface();
		this.statusBarPanel.updateInterface();

		repaint();
	}

	@Override
	public void dispose()
	{
		this.turnMode = State.CLOSED;
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

	public StatusBarPanel getStatusBarPanel()
	{
		return this.statusBarPanel;
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
