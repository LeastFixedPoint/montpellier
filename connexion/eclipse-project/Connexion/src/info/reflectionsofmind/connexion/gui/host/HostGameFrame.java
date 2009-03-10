package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.gui.JConnexionFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.platform.common.Participant;
import info.reflectionsofmind.connexion.platform.common.Participant.State;
import info.reflectionsofmind.connexion.platform.server.IRemoteClient;
import info.reflectionsofmind.connexion.platform.server.IServer;
import info.reflectionsofmind.connexion.platform.server.ServerUtil;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JConnexionFrame implements ChatPane.IListener, IServer.IListener, Participant.IStateListener
{
	private static final long serialVersionUID = 1L;
	private final IServer server;

	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final ClientsPanel clientsPanel;
	private final TransportsPanel transportsPanel;
	private final ChatPane chatPane;

	public HostGameFrame(IServer server)
	{
		super(server.getApplication());

		this.server = server;

		setTitle("Connexion :: Host game");
		getServer().addListener(this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(true);
		setLayout(new MigLayout("", "[center, shrink 0]6[grow]", "[shrink 0]6[shrink 0]6[shrink 0]6[top, grow]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "grow, wrap");

		this.transportsPanel = new TransportsPanel(this);
		add(this.transportsPanel, "grow, wrap");

		this.startButton = new JButton(new StartAction());
		add(this.startButton, "wrap, w 120");

		this.clientsPanel = new ClientsPanel(this);
		add(this.clientsPanel, "grow, wrap, h 240");

		this.chatPane = new ChatPane();
		this.chatPane.addListener(this);
		add(this.chatPane, "grow, cell 1 0, spany 4, w 480");

		pack();
		setLocationRelativeTo(null);
		setMinimumSize(getSize());
	}

	@Override
	public void onChatPaneMessageSent(final String message)
	{
		this.chatPane.writeMessage(null, message);
		this.server.sendMessage(message);
	}

	@Override
	public void onClientConnected(final IRemoteClient client)
	{
		client.getParticipant().addStateListener(this);
		this.chatPane.writeSystem(ChatPane.format(client.getParticipant()) + " connected.");
	}

	@Override
	public void onClientMessage(final IRemoteClient client, final String message)
	{
		this.chatPane.writeMessage(client.getParticipant(), message);
	}

	@Override
	public void onAfterClientStateChange(final Participant participant, final State previousState)
	{
		switch (participant.getState())
		{
			case ACCEPTED:
				this.chatPane.writeSystem(ChatPane.format(participant) + " was accepted into the game as player.");
				break;
			case SPECTATOR:
				this.chatPane.writeSystem(ChatPane.format(participant) + " was accepted into the game as spectator.");
				break;
			case CONNECTED:
				this.chatPane.writeSystem(ChatPane.format(participant) + " was rejected from game (will not participate).");
				break;
		}
	}

	@Override
	public void onAfterClientDisconnected(final IRemoteClient client)
	{
		this.chatPane.writeSystem(ChatPane.format(client.getParticipant()) + " disconnected.");
	}

	// ====================================================================================================
	// === GETTERS AND SETTERS
	// ====================================================================================================

	public IServer getServer()
	{
		return this.server;
	}

	public ChatPane getChatPane()
	{
		return this.chatPane;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

	private class StartAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public StartAction()
		{
			super("Start game");
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			if (ServerUtil.getClientsByStates(getServer(), Participant.State.ACCEPTED).isEmpty())
			{
				JOptionPane.showMessageDialog(HostGameFrame.this, "You must have at least one player!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			HostGameFrame.this.configPanel.setEnabled(false);
			HostGameFrame.this.clientsPanel.setEnabled(false);

			setEnabled(false);

			HostGameFrame.this.server.startGame();
		}
	}
}
