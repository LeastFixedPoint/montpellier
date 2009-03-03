package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Participant;
import info.reflectionsofmind.connexion.common.Participant.State;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.gui.JConnexionFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.transport.IClientTransport;
import info.reflectionsofmind.connexion.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.local.LocalClientTransport;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JConnexionFrame implements IClient.IListener, Participant.IStateListener, ChatPane.IListener
{
	private final IClient client;

	private final JLabel statusLabel;
	private final TransportComboBox transportCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final PlayerList playerList;

	public JoinGameFrame(final IClient client)
	{
		super(client.getApplication());

		setTitle("Connexion :: Join game");

		this.client = client;

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

	public void connect(final IClientTransport transport)
	{
		transportCombo.setEnabled(false);
		connectButton.setAction(new DisconnectAction());
		statusLabel.setText("Connecting...");

		new Thread()
		{
			@Override
			public void run()
			{
				if (transport instanceof LocalClientTransport)
				{
					getClient().setName(((LocalClientTransport) transport).getClientName());
				}

				JoinGameFrame.this.chatPane.writeSystem("Starting " + ChatPane.format(transport) + " transport...");

				try
				{
					transport.start();
				}
				catch (TransportException exception)
				{
					throw new RuntimeException(exception);
				}

				JoinGameFrame.this.chatPane.writeSystem("Connecting...");

				getClient().connect(transport);
			}
		}.start();
	}

	public void connect()
	{
		final IClientTransportFactory transportFactory = this.transportCombo.getSelectedTransportFactory();
		final Form form = transportFactory.newConfigurationForm();
		new FormDialog(this, form, "Server configuration", "Connect")
		{
			@Override
			protected void onSubmit()
			{
				try
				{
					connect(transportFactory.createTransport(form));
				}
				catch (TransportException exception)
				{
					throw new RuntimeException(exception);
				}
			}
		}.setVisible(true);
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
		this.chatPane.writeSystem("Connected.");

		if (!getClient().getParticipants().isEmpty())
		{
			final String alreadyPresent = Util.join(ChatPane.format(getClient().getParticipants()), ", ");
			this.chatPane.writeSystem("Already present: " + alreadyPresent + ".");
		}
		else
		{
			this.chatPane.writeSystem("Already present: no one.");
		}

		this.chatPane.setEnabled(true);
		this.statusLabel.setText("Connected");
	}

	@Override
	public void onConnectionBroken(final DisconnectReason reason)
	{
		this.chatPane.writeSystem("Disconnected. Reason: " + ChatPane.format(reason) + ".");

		this.chatPane.setEnabled(false);
		this.transportCombo.setEnabled(true);
		this.connectButton.setAction(new ConnectAction());
		this.statusLabel.setText("Disconnected");
	}

	@Override
	public void onClientConnected(final Participant client)
	{
		if (client == getClient().getParticipant())
		{
			this.chatPane.writeSystem("You have connected as " + ChatPane.format(client) + ".");
		}
		else
		{
			this.chatPane.writeSystem(ChatPane.format(client) + " connected.");
		}
		
		client.addStateListener(this);
	}

	@Override
	public void onClientDisconnected(final Participant client)
	{
		this.chatPane.writeSystem(ChatPane.format(client) + " disconnected.");
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
