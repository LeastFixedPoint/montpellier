package info.reflectionsofmind.connexion.local.client.gui.join;

import info.reflectionsofmind.connexion.MainWindow;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.ClientUtil;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.local.client.gui.play.GameWindow;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.transport.TransportException;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class JoinGameDialog extends JDialog implements IRemoteServer.IListener
{
	private final List<ServerType> serverTypes = new ArrayList<ServerType>();

	private final IClient client;
	private final MainWindow parent;

	private final JLabel statusLabel;
	private final JComboBox connectionTypeCombo;
	private final JButton connectButton;
	private final ChatPane chatPane;
	private final JList playerList;

	public JoinGameDialog(final MainWindow parent)
	{
		super(parent, "Connexion :: Join game", true);

		this.parent = parent;
		this.client = new DefaultLocalClient(this.parent.getApplication().getSettings());

		final JabberTransport jabberTransport = new JabberTransport(client.getSettings().getJabberAddress());
		this.serverTypes.add(new ServerType(jabberTransport.getName(), jabberTransport, new JabberConnector()));

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
		add(this.chatPane, "grow, span 3");

		this.playerList = new JList(new PlayersModel());
		add(new JScrollPane(this.playerList), "grow, wrap");

		pack();
		setLocationRelativeTo(null);

		this.chatPane.writeSystem("Please select connection type and click \"Connect\".");
	}

	public void connect()
	{
		for (ServerType serverType : this.serverTypes)
		{
			if (serverType == this.connectionTypeCombo.getSelectedItem())
			{
				serverType.getConnector().connect();

				JoinGameDialog.this.connectButton.setEnabled(false);
				JoinGameDialog.this.connectionTypeCombo.setEnabled(false);
				
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
			return JoinGameDialog.this.client.getPlayers().get(index);
		}

		@Override
		public int getSize()
		{
			final IClient client = JoinGameDialog.this.client;
			return client == null ? 0 : client.getPlayers().size();
		}
	}

	public interface IConnector
	{
		void connect();
	}

	public final class JabberConnector implements IConnector
	{
		@Override
		public void connect()
		{
			final JabberConfigurationDialog dialog = new JabberConfigurationDialog(JoinGameDialog.this);
			dialog.setVisible(true);

			new Thread()
			{
				@Override
				public void run()
				{
					final JabberAddress jabberServer = dialog.getServer();
					final JabberAddress jabberClient = JoinGameDialog.this.parent.getApplication().getSettings().getJabberAddress();

					JoinGameDialog.this.chatPane.writeSystem("Connecting");
					JoinGameDialog.this.chatPane.writeSystem("&nbsp;&nbsp;&nbsp;&nbsp;as: " + jabberClient.asString());
					JoinGameDialog.this.chatPane.writeSystem("&nbsp;&nbsp;&nbsp;&nbsp;to: " + jabberServer.asString());

					if (jabberServer == null || jabberClient == null) return;

					final JabberTransport transport = ClientUtil.findTransport(client, JabberTransport.class);
					final JabberNode serverNode = transport.new JabberNode(jabberServer);
					final IRemoteServer server = new RemoteServer<JabberTransport, JabberNode>(transport, serverNode);

					try
					{
						JoinGameDialog.this.chatPane.writeSystem("Initializing connection...");
						server.start();
						JoinGameDialog.this.chatPane.writeSystem("Connection established.");
					}
					catch (TransportException exception)
					{
						exception.printStackTrace();
						JoinGameDialog.this.chatPane.writeSystem("Connection failed.");
						return;
					}
					
					try
					{
						JoinGameDialog.this.chatPane.writeSystem("Sending connection request... ");
						client.connect(server);
						JoinGameDialog.this.chatPane.writeSystem("Connection request sent.");
						JoinGameDialog.this.chatPane.writeSystem("Waiting for response...");
					}
					catch (final ServerConnectionException exception)
					{
						exception.printStackTrace();
						JoinGameDialog.this.chatPane.writeSystem("Cannot connect to the server.");
						return;
					}
					catch (final RemoteServerException exception)
					{
						exception.printStackTrace();
						JoinGameDialog.this.chatPane.writeSystem("Server-side failure.");
						return;
					}
				}
			}.start();
		}
	}
}
