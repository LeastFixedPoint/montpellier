package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.gui.MainFrame;
import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.gui.play.GameWindow;
import info.reflectionsofmind.connexion.local.client.ClientUtil;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.LocalRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport.JabberNode;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JFrame implements IRemoteServer.IListener, ChatPane.IListener
{
	private final List<ServerType> serverTypes = new ArrayList<ServerType>();

	private final IClient client;
	private final MainFrame mainFrame;

	private final JLabel statusLabel;
	private final JComboBox connectionTypeCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final JList playerList;

	public JoinGameFrame(final MainFrame mainFrame, final LocalRemoteServer remoteServer)
	{
		super("Connexion :: Join game");

		this.mainFrame = mainFrame;
		this.client = new DefaultLocalClient(this.mainFrame.getApplication().getSettings());

		if (remoteServer == null)
		{
			final JabberTransport jabberTransport = new JabberTransport(client.getSettings().getJabberAddress());
			this.serverTypes.add(new ServerType(jabberTransport.getName(), jabberTransport, new JabberConnector()));
		}
		else
		{
			this.serverTypes.add(new ServerType(remoteServer.getTransport().getName(), remoteServer.getTransport(), new LocalConnector(remoteServer)));
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[120]6[120]6[120]6[120]", "[]6[360]"));

		this.connectionTypeCombo = new JComboBox(this.serverTypes.toArray());
		add(this.connectionTypeCombo, "grow");

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
		this.chatPane.setEnabled(remoteServer != null);
		this.chatPane.addListener(this);
		add(this.chatPane, "grow, span 3");

		this.playerList = new JList(new PlayersModel());
		add(new JScrollPane(this.playerList), "grow, wrap");

		pack();
		setLocationRelativeTo(null);
	}

	public JoinGameFrame(final MainFrame parent)
	{
		this(parent, null);
	}

	@Override
	public void onChatPaneMessageSent(String message)
	{
		try
		{
			client.getServer().sendEvent(new ClientToServer_MessageEvent(message));
		}
		catch (ServerConnectionException exception)
		{
			exception.printStackTrace();
			chatPane.writeSystem("Connection failure when sending message.");
		}
		catch (RemoteServerException exception)
		{
			exception.printStackTrace();
			chatPane.writeSystem("Server-side error when sending message.");
		}
	}
	
	public void connect()
	{
		for (ServerType serverType : this.serverTypes)
		{
			if (serverType == this.connectionTypeCombo.getSelectedItem())
			{
				serverType.getConnector().connect();

				JoinGameFrame.this.connectButton.setEnabled(false);
				JoinGameFrame.this.connectionTypeCombo.setEnabled(false);

				return;
			}
		}

		this.chatPane.writeSystem("Error: unknown server type [" + this.connectionTypeCombo.getSelectedItem() + "].");
		return;
	}

	// ============================================================================================
	// === SERVER-TO-CLIENT EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		this.statusLabel.setText("Connection request accepted.");

		this.chatPane.setEnabled(true);
		this.playerList.setEnabled(true);
	}

	@Override
	public void onPlayerConnected(final ServerToClient_PlayerConnectedEvent event)
	{

	}

	@Override
	public void onPlayerDisconnected(final ServerToClient_PlayerDisconnectedEvent event)
	{

	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{
		this.client.getServer().removeListener(this);
		dispose();
		new GameWindow(this.client).setVisible(true);
	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{
		this.statusLabel.setText("Desynchronization error: game already started.");
	}

	@Override
	public void onMessage(final ServerToClient_MessageEvent event)
	{
		final String name = this.client.getPlayers().get(event.getPlayerIndex()).getName();
		this.chatPane.writeMessage(name, event.getMessage());
	}

	// ============================================================================================
	// === MODELS
	// ============================================================================================

	private final class PlayersModel extends AbstractListModel
	{
		@Override
		public Object getElementAt(final int index)
		{
			return JoinGameFrame.this.client.getPlayers().get(index);
		}

		@Override
		public int getSize()
		{
			final IClient client = JoinGameFrame.this.client;
			return client == null ? 0 : client.getPlayers().size();
		}
	}

	public interface IConnector
	{
		void connect();
	}

	public final class LocalConnector implements IConnector
	{
		private final LocalRemoteServer server;
		
		public LocalConnector(LocalRemoteServer server)
		{
			this.server = server;
		}

		@Override
		public void connect()
		{
			try
			{
				JoinGameFrame.this.chatPane.writeSystem("Launched in local client mode.");
				server.start();
				client.connect(server);
			}
			catch (final ServerConnectionException exception)
			{
				exception.printStackTrace();
				JoinGameFrame.this.chatPane.writeSystem("Error.");
				return;
			}
			catch (final RemoteServerException exception)
			{
				exception.printStackTrace();
				JoinGameFrame.this.chatPane.writeSystem("Error.");
				return;
			}
		}
	}

	public final class JabberConnector implements IConnector
	{
		@Override
		public void connect()
		{
			final JabberConfigurationDialog dialog = new JabberConfigurationDialog(JoinGameFrame.this);
			dialog.setVisible(true);

			new Thread()
			{
				@Override
				public void run()
				{
					final JabberAddress jabberServer = dialog.getServer();
					final JabberAddress jabberClient = JoinGameFrame.this.mainFrame.getApplication().getSettings().getJabberAddress();

					JoinGameFrame.this.chatPane.writeSystem("Connecting");
					JoinGameFrame.this.chatPane.writeSystem("&nbsp;&nbsp;&nbsp;&nbsp;as: " + jabberClient.asString());
					JoinGameFrame.this.chatPane.writeSystem("&nbsp;&nbsp;&nbsp;&nbsp;to: " + jabberServer.asString());

					if (jabberServer == null || jabberClient == null) return;

					final JabberTransport transport = ClientUtil.findTransport(client, JabberTransport.class);
					final JabberNode serverNode = transport.new JabberNode(jabberServer);
					final IRemoteServer server = new RemoteServer<JabberTransport, JabberNode>(transport, serverNode);

					try
					{
						JoinGameFrame.this.chatPane.writeSystem("Initializing connection...");
						server.start();
						JoinGameFrame.this.chatPane.writeSystem("Connection established.");
					}
					catch (final ServerConnectionException exception)
					{
						exception.printStackTrace();
						JoinGameFrame.this.chatPane.writeSystem("Connection failed.");
						return;
					}

					try
					{
						JoinGameFrame.this.chatPane.writeSystem("Sending connection request... ");
						client.connect(server);
						JoinGameFrame.this.chatPane.writeSystem("Connection request sent.");
						JoinGameFrame.this.chatPane.writeSystem("Waiting for response...");
					}
					catch (final ServerConnectionException exception)
					{
						exception.printStackTrace();
						JoinGameFrame.this.chatPane.writeSystem("Cannot connect to the server.");
						return;
					}
					catch (final RemoteServerException exception)
					{
						exception.printStackTrace();
						JoinGameFrame.this.chatPane.writeSystem("Server-side failure.");
						return;
					}
				}
			}.start();
		}
	}
}
