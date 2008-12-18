package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.ILocalClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JFrame implements ILocalClient.IListener, Client.IStateListener, ChatPane.IListener
{
	private final ILocalClient client;

	private final JLabel statusLabel;
	private final JComboBox transportCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final JList playerList;

	public JoinGameFrame(final Settings settings)
	{
		super("Connexion :: Join game");

		this.client = new DefaultLocalClient(settings);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[120]6[120]6[120]6[120]", "[]6[360]"));

		this.transportCombo = new JComboBox(getClient().getTransports().toArray());
		add(this.transportCombo, "grow");

		this.connectButton = new JButton(new AbstractAction("Connect...")
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				connect();
			}
		});
		add(this.connectButton, "grow");

		this.statusLabel = new JLabel("Not connected");
		add(this.statusLabel, "grow, span");

		this.chatPane = new ChatPane();
		this.chatPane.setEnabled(false);
		this.chatPane.addListener(this);
		add(this.chatPane, "grow, span 3");

		this.playerList = new JList(new PlayersModel());
		add(new JScrollPane(this.playerList), "grow, wrap");

		pack();
		setLocationRelativeTo(null);
	}

	public void connect(INode serverNode)
	{
		getClient().connect(serverNode);
	}

	public void connect()
	{
		final String serverNodeId = JOptionPane.showInputDialog(this, "Enter server address");

		if (serverNodeId == null) return;

		final ITransport transport = (ITransport) this.transportCombo.getSelectedItem();

		connect(transport.getNode(serverNodeId));
	}

	// ============================================================================================
	// === CLIENT EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAccepted()
	{
		this.chatPane.writeSystem("Connected to [" + getClient().getServerNode() + "].");
	}

	@Override
	public void onClientConnected(Client client)
	{
		this.chatPane.writeSystem("[" + client.getName() + "] connected.");
		client.addStateListener(this);
	}

	@Override
	public void onClientDisconnected(Client client)
	{
		this.chatPane.writeSystem("[" + client.getName() + "] disconnected.");
	}

	@Override
	public void onChatMessage(Client sender, String message)
	{
		this.chatPane.writeMessage(sender.getName(), message);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onTurn(Turn turn, String nextTileCode)
	{

	}

	@Override
	public void onConnectionBroken(DisconnectReason reason)
	{
		this.chatPane.writeSystem("Disconnected. Reason: [" + reason + "].");
	}

	// ====================================================================================================
	// === OTHER HANDLERS
	// ====================================================================================================

	@Override
	public void onChatPaneMessageSent(String message)
	{
		getClient().sendChatMessage(message);
	}

	@Override
	public void onAfterClientStateChange(Client client, State previousState)
	{
		this.chatPane.writeSystem("[" + client + "] is now [" + client.getState() + "].");
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	public ILocalClient getClient()
	{
		return this.client;
	}

	// ============================================================================================
	// === MODELS
	// ============================================================================================

	private final class PlayersModel extends AbstractListModel
	{
		@Override
		public Object getElementAt(final int index)
		{
			return JoinGameFrame.this.client.getClients().get(index);
		}

		@Override
		public int getSize()
		{
			final ILocalClient client = JoinGameFrame.this.client;
			return client == null ? 0 : client.getClients().size();
		}
	}
}
