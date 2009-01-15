package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.client.DefaultClient;
import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.common.Participant;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.common.Participant.State;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.gui.JConnexionFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
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
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class JoinGameFrame extends JConnexionFrame implements IClient.IListener, Participant.IStateListener, ChatPane.IListener
{
	private final IClient client;

	private final JLabel statusLabel;
	private final TransportComboBox transportCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final PlayerList playerList;

	public JoinGameFrame(final IApplication application)
	{
		setTitle("Connexion :: Join game");

		this.client = createChild("client", DefaultClient.class);

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
					JoinGameFrame.this.chatPane.writeSystem("Starting " + ChatPane.format(serverNode.getTransport()) + " transport...");
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

				JoinGameFrame.this.chatPane.writeSystem("Sending connection request to " + ChatPane.format(serverNode) + "...");
				getClient().connect(serverNode);
			}
		}.start();
	}

	public void connect()
	{
		final ITransport transport = this.transportCombo.getSelectedTransport();

		final ChooseServerDialog dialog = new ChooseServerDialog(this, transport);
		dialog.setVisible(true);

		if (dialog.getServerNode() != null)
		{
			connect(dialog.getServerNode());
		}
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
		this.chatPane.writeSystem("Connected to " + ChatPane.format(getClient().getServerNode()) + ".");

		if (!getClient().getParticipants().isEmpty())
		{
			final String alreadyPresent = Util.join(Lists.transform(getClient().getParticipants(), new Function<Participant, String>()
			{
				@Override
				public String apply(final Participant client)
				{
					return ChatPane.format(client);
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
		this.chatPane.writeSystem("Disconnected. Reason: " + reason + ".");

		this.chatPane.setEnabled(false);
		this.transportCombo.setEnabled(true);
		this.connectButton.setAction(new ConnectAction());
		this.statusLabel.setText("Disconnected");
	}

	@Override
	public void onClientConnected(final Participant client)
	{
		this.chatPane.writeSystem(ChatPane.format(client) + " connected.");
		client.addStateListener(this);
	}

	@Override
	public void onClientDisconnected(final Participant client)
	{
		this.chatPane.writeSystem(ChatPane.format(client) + " sdisconnected.");
	}

	@Override
	public void onChatMessage(final Participant sender, final String message)
	{
		this.chatPane.writeMessage(sender, message);
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
	public void onAfterClientStateChange(final Participant client, final State previousState)
	{
		this.chatPane.writeSystem(ChatPane.format(client) + " is now [" + client.getState() + "].");
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	public IClient getClient()
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
