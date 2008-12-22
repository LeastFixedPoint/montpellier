package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.gui.MainFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.server.DefaultLocalServer;
import info.reflectionsofmind.connexion.server.IRemoteClient;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.server.ServerUtil;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JFrame implements ChatPane.IListener, IServer.IListener, Client.IStateListener
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
		super("Connexion :: Host game");

		this.mainWindow = mainWindow;
		this.server = new DefaultLocalServer(mainWindow.getApplication().getSettings());
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
		this.server.addListener(this.clientsPanel);
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
		this.chatPane.writeMessage("Server", message);
	}

	@Override
	public void onClientConnected(final IRemoteClient client)
	{
		client.getClient().addStateListener(this);
		this.chatPane.writeSystem("<b>" + client.getClient().getName() + "</b> connected.");
	}

	@Override
	public void onClientMessage(final IRemoteClient client, final String message)
	{
		this.chatPane.writeMessage(client.getClient().getName(), message);
	}

	@Override
	public void onAfterClientStateChange(final Client client, final State previousState)
	{
		switch (client.getState())
		{
			case ACCEPTED:
				this.chatPane.writeSystem("<b>" + client.getName() + "</b> was accepted into the game as <b>player</b>.");
				break;
			case SPECTATOR:
				this.chatPane.writeSystem("<b>" + client.getName() + "</b> was accepted into the game as <b>spectator</b>.");
				break;
			case CONNECTED:
				this.chatPane.writeSystem("<b>" + client.getName() + "</b> was rejected from game (will not participate).");
				break;
		}
	}
	
	@Override
	public void onClientDisconnected(IRemoteClient client)
	{
		this.chatPane.writeSystem("<b>" + client.getClient().getName() + "</b> disconnected.");
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
			if (ServerUtil.getClientsByStates(getServer(), Client.State.ACCEPTED).isEmpty())
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
