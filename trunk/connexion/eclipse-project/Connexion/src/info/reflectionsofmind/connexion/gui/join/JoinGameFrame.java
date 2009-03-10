package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.gui.JConnexionFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.gui.play.GameWindow;
import info.reflectionsofmind.connexion.platform.client.IClient;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.common.Participant;
import info.reflectionsofmind.connexion.platform.common.Participant.State;
import info.reflectionsofmind.connexion.platform.transport.IClientTransport;
import info.reflectionsofmind.connexion.platform.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.platform.transport.TransportException;
import info.reflectionsofmind.connexion.platform.transport.local.LocalClientTransport;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JConnexionFrame implements IClient.IListener, Participant.IStateListener, ChatPane.IListener
{
	private final IClient client;

	private final JLabel statusLabel;
	private final TransportComboBox transportCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final PlayerList playerList;

	private IClientTransport transport;

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
		this.transportCombo.setEnabled(false);
		this.connectButton.setAction(new DisconnectAction());
		this.statusLabel.setText("Connecting...");

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
				catch (final TransportException exception)
				{
					throw new RuntimeException(exception);
				}

				JoinGameFrame.this.chatPane.writeSystem("Connecting to server...");

				JoinGameFrame.this.transport = transport;
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
				catch (final TransportException exception)
				{
					throw new RuntimeException(exception);
				}
			}
		}.setVisible(true);
	}

	public void disconnect()
	{
		getClient().disconnect(DisconnectReason.CLIENT_REQUEST);
		this.transport.stop();
		this.transport = null;
	}

	// ============================================================================================
	// === CLIENT EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAccepted()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JoinGameFrame.this.chatPane.setEnabled(true);
				JoinGameFrame.this.statusLabel.setText("Connected");
				JoinGameFrame.this.chatPane.writeSystem("Connected as " + ChatPane.format(JoinGameFrame.this.client.getParticipant()) + ".");
			}
		});
	}

	@Override
	public void onAfterConnectionBroken(final DisconnectReason reason)
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
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JoinGameFrame.this.chatPane.writeSystem(ChatPane.format(client) + " connected.");
			}
		});

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
		new GameWindow(this.client).setVisible(true);
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
