package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.gui.MainFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.local.server.DefaultLocalServer;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.local.server.IServer.IClientListener;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JFrame implements ChatPane.IListener, IClientListener
{
	private static final long serialVersionUID = 1L;
	private final DefaultLocalServer server;

	private final MainFrame mainWindow;
	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final ClientsPanel clientsPanel;
	private final ChatPane chatPane;

	public HostGameFrame(final MainFrame mainWindow)
	{
		super("Connexion :: Host game");

		this.mainWindow = mainWindow;
		this.server = new DefaultLocalServer(mainWindow.getApplication().getSettings());
		this.server.addPlayerListener(this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(true);
		setLayout(new MigLayout("", "[center, shrink 0]6[grow]", "[shrink 0]6[shrink 0]6[top, grow]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "grow, wrap");

		this.clientsPanel = new ClientsPanel(this);
		add(this.clientsPanel, "grow, wrap");

		this.startButton = new JButton(new StartAction());
		add(this.startButton, "wrap, w 120");

		this.chatPane = new ChatPane();
		this.chatPane.addListener(this);
		add(this.chatPane, "grow, cell 1 0, spany 3, w 480");

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
	public void onAfterClientConnected(ISlot slot)
	{
	}

	@Override
	public void onBeforeClientDisconnected(ISlot slot, DisconnectReason reason)
	{
	}

	@Override
	public void onMessage(Player player, String message)
	{
		this.chatPane.writeMessage(player.getName(), message);
	}

	// ====================================================================================================
	// === GETTERS AND SETTERS
	// ====================================================================================================

	public DefaultLocalServer getServer()
	{
		return this.server;
	}

	public MainFrame getMainWindow()
	{
		return this.mainWindow;
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
			if (ServerUtil.getPlayers(getServer()).isEmpty())
			{
				JOptionPane.showMessageDialog(HostGameFrame.this, "You must have at least one player!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			HostGameFrame.this.configPanel.setEnabled(false);
			HostGameFrame.this.clientsPanel.setEnabled(false);

			setEnabled(false);

			HostGameFrame.this.server.startGame(HostGameFrame.this.configPanel.getGameName());
		}
	}
}
