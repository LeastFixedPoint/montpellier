package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.common.Participant;
import info.reflectionsofmind.connexion.common.Participant.State;
import info.reflectionsofmind.connexion.gui.JConnexionFrame;
import info.reflectionsofmind.connexion.gui.MainFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.server.DefaultServer;
import info.reflectionsofmind.connexion.server.IRemoteClient;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.server.ServerUtil;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JConnexionFrame implements ChatPane.IListener, IServer.IListener, Participant.IStateListener
{
	private static final long serialVersionUID = 1L;
	private final IServer server;

	private final MainFrame mainWindow;
	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final ClientsPanel clientsPanel;
	private final TransportsPanel transportsPanel;
	private final ChatPane chatPane;

	public HostGameFrame(final MainFrame mainWindow)
	{
		setTitle("Connexion :: Host game");

		this.mainWindow = mainWindow;
		this.server = createChild("server", DefaultServer.class);
		this.server.addListener(this);

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
		client.getClient().addStateListener(this);
		this.chatPane.writeSystem("[<red>" + client.getClient().getName() + "</red>] connected.");
	}

	@Override
	public void onClientMessage(final IRemoteClient client, final String message)
	{
		this.chatPane.writeMessage(client.getClient(), message);
	}

	@Override
	public void onAfterClientStateChange(final Participant client, final State previousState)
	{
		switch (client.getState())
		{
			case ACCEPTED:
				this.chatPane.writeSystem("[<red>" + client.getName() + "</red>] was accepted into the game as player.");
				break;
			case SPECTATOR:
				this.chatPane.writeSystem("[<red>" + client.getName() + "</red>] was accepted into the game as spectator.");
				break;
			case CONNECTED:
				this.chatPane.writeSystem("[<red>" + client.getName() + "</red>] was rejected from game (will not participate).");
				break;
		}
	}

	@Override
	public void onClientDisconnected(final IRemoteClient client)
	{
		this.chatPane.writeSystem("[<red>" + client.getClient().getName() + "</red>] disconnected.");
	}

	// ====================================================================================================
	// === GETTERS AND SETTERS
	// ====================================================================================================

	public IServer getServer()
	{
		return this.server;
	}

	public MainFrame getMainWindow()
	{
		return this.mainWindow;
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
