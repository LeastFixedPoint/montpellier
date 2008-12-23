package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.client.ILocalClient;
import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.gui.common.TransportName;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport.ClientToServerNode;
import info.reflectionsofmind.connexion.util.Util;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class JoinGameFrame extends JFrame implements ILocalClient.IListener, Client.IStateListener, ChatPane.IListener
{
	private final ILocalClient client;

	private final JLabel statusLabel;
	private final TransportComboBox transportCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final PlayerList playerList;

	public JoinGameFrame(final Settings settings)
	{
		super("Connexion :: Join game");

		this.client = new DefaultLocalClient(settings);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[120]6[120]6[120]6[120]", "[]6[360]"));

		this.transportCombo = new TransportComboBox(this);
		add(this.transportCombo, "grow");

		this.connectButton = new JButton(new ConnectAction());
		add(this.connectButton, "grow");

		this.statusLabel = new JLabel("Not connected");
		add(this.statusLabel, "grow, span");

		this.chatPane = new ChatPane();
		this.chatPane.setEnabled(false);
		this.chatPane.addListener(this);
		add(this.chatPane, "grow, span 3");

		this.playerList = new PlayerList(this);
		add(new JScrollPane(this.playerList), "grow, wrap");

		pack();
		setLocationRelativeTo(null);

		this.client.addListener(this);
		this.client.addListener(this.playerList);
	}

	public void connect(final INode serverNode)
	{
		transportCombo.setEnabled(false);
		connectButton.setAction(new DisconnectAction());
		statusLabel.setText("Connecting...");
		
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					JoinGameFrame.this.chatPane.writeSystem("Starting " + TransportName.getName(serverNode.getTransport()) + " transport...");
					serverNode.getTransport().start();
					JoinGameFrame.this.chatPane.writeSystem("Transport started.");
				}
				catch (final TransportException exception)
				{
					exception.printStackTrace();
					JoinGameFrame.this.chatPane.writeSystem("Cannot start transport.");
					return;
				}

				if (serverNode instanceof ClientToServerNode)
				{
					final int i = ((ClientToServerNode) serverNode).getIndex();
					getClient().setName(getClient().getName() + "." + i);
				}

				JoinGameFrame.this.chatPane.writeSystem("Sending connection request to [" + serverNode.getId() + "]...");
				getClient().connect(serverNode);
				JoinGameFrame.this.chatPane.writeSystem("Connection request sent, waiting for reply...");
			}
		}.start();
	}

	public void connect()
	{
		final String serverNodeId = JOptionPane.showInputDialog(this, "Enter server address");

		if (serverNodeId == null) return;

		final ITransport transport = this.transportCombo.getSelectedTransport();

		new ChooseServerDialog(this, transport).setVisible(true);
		
		//connect(transport.getNode(serverNodeId));
	}

	public void disconnect()
	{
		getClient().disconnect(DisconnectReason.CLIENT_REQUEST);
		onConnectionBroken(DisconnectReason.CLIENT_REQUEST);
	}

	// ============================================================================================
	// === CLIENT EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAccepted()
	{
		this.chatPane.writeSystem("Connected to [" + getClient().getServerNode().getId() + "].");

		if (!getClient().getClients().isEmpty())
		{
			final String alreadyPresent = Util.join(Lists.transform(getClient().getClients(), new Function<Client, String>()
			{
				@Override
				public String apply(final Client client)
				{
					return "[<red>" + client.getName() + "</red>]";
				}
			}), ", ");

			this.chatPane.writeSystem("Already present: " + alreadyPresent + ".");
		}

		this.chatPane.setEnabled(true);
		this.statusLabel.setText("Connected");
	}

	@Override
	public void onConnectionBroken(final DisconnectReason reason)
	{
		this.chatPane.writeSystem("Disconnected. Reason: [" + reason + "].");

		this.chatPane.setEnabled(false);
		this.transportCombo.setEnabled(true);
		this.connectButton.setAction(new ConnectAction());
		this.statusLabel.setText("Disconnected");
	}

	@Override
	public void onClientConnected(final Client client)
	{
		this.chatPane.writeSystem("[<red>" + client.getName() + "</red>] connected.");
		client.addStateListener(this);
	}

	@Override
	public void onClientDisconnected(final Client client)
	{
		this.chatPane.writeSystem("[" + client.getName() + "] disconnected.");
	}

	@Override
	public void onChatMessage(final Client sender, final String message)
	{
		this.chatPane.writeMessage(sender.getName(), message);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onTurn(final Turn turn, final String nextTileCode)
	{

	}

	// ====================================================================================================
	// === OTHER HANDLERS
	// ====================================================================================================

	@Override
	public void onChatPaneMessageSent(final String message)
	{
		getClient().sendChatMessage(message);
	}

	@Override
	public void onAfterClientStateChange(final Client client, final State previousState)
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
	// === ACTIONS AND MODELS
	// ============================================================================================

	private final class ConnectAction extends AbstractAction
	{
		private ConnectAction()
		{
			super("Connect...");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			connect();
		}
	}

	private final class DisconnectAction extends AbstractAction
	{
		private DisconnectAction()
		{
			super("Disconnect");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			disconnect();
		}
	}
}
