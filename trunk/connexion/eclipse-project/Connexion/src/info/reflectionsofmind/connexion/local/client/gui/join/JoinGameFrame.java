package info.reflectionsofmind.connexion.local.client.gui.join;

import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.local.client.gui.play.ClientUI;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteJabberServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JFrame implements IRemoteServer.IListener
{
	public static enum ConnectionType
	{
		JABBER
	}

	private IClient client;

	private final JLabel statusLabel;
	private final JComboBox connectionTypeCombo;
	private final JButton connectButton;
	private final JEditorPane chatPane;
	private final JTextField sendField;
	private final JButton sendButton;
	private final JList playerList;

	public JoinGameFrame()
	{
		super("Connexion :: Join game");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[120]6[120]6[120]6[120]", "[]6[360]6[]"));

		this.connectionTypeCombo = new JComboBox(ConnectionType.values());
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

		this.chatPane = new JEditorPane("text/html", "");
		this.chatPane.setEditable(false);
		this.chatPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		add(this.chatPane, "grow, span 3");

		final HTMLDocument document = (HTMLDocument) JoinGameFrame.this.chatPane.getDocument();
		
		try
		{
			document.setOuterHTML(document.getRootElements()[0].getElement(0), 
					"<body style='margin: 0px; padding: 0px;'><div>Initialization ok.</div></body>");
		}
		catch (BadLocationException exception)
		{
			exception.printStackTrace();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		
		this.playerList = new JList(new PlayersModel());
		add(new JScrollPane(this.playerList), "grow, wrap, spany 2");

		this.sendField = new JTextField();
		add(this.sendField, "grow, span 3, split");

		this.sendButton = new JButton("Send");
		add(this.sendButton);

		pack();
		setLocationRelativeTo(null);
	}

	private void writeSystem(final String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final HTMLDocument document = (HTMLDocument) JoinGameFrame.this.chatPane.getDocument();
					document.insertBeforeEnd(document.getRootElements()[0].getElement(0), // 
							"<p style='color:green;padding:0px;margin:0px;'>" + text + "</p>");
				}
				catch (final IOException exception)
				{
					exception.printStackTrace();
				}
				catch (final BadLocationException exception)
				{
					exception.printStackTrace();
				}
			}
		});
	}

	public void connect()
	{
		final JabberConfigurationDialog dialog = new JabberConfigurationDialog(this);
		dialog.setVisible(true);

		new Thread()
		{
			@Override
			public void run()
			{
				final JabberAddress jabberServer = dialog.getServer();
				final JabberAddress jabberClient = dialog.getClient();

				writeSystem("Connecting") 
				writeSystem("to: " + jabberServer.getAddress());
				writeSystem("as: " + jabberClient.getAddress());

				if (jabberServer == null || jabberClient == null) return;

				JoinGameFrame.this.connectButton.setEnabled(false);
				JoinGameFrame.this.connectionTypeCombo.setEnabled(false);

				final IClient client = new DefaultLocalClient("DLC");
				final IRemoteServer server = new RemoteJabberServer(jabberClient, jabberServer);

				try
				{
					writeSystem("Sending connection request... ");
					client.connect(server);
					writeSystem("Sent.");
				}
				catch (final ServerConnectionException exception)
				{
					exception.printStackTrace();
					writeSystem("Cannot connect to the server.");
					return;
				}
				catch (final RemoteServerException exception)
				{
					exception.printStackTrace();
					writeSystem("Server-side failure.");
					return;
				}

				JoinGameFrame.this.client = client;

			}
		}.start();
	}

	// ============================================================================================
	// === SERVER-TO-CLIENT EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		this.statusLabel.setText("Server accepted connection.");

		this.chatPane.setEnabled(true);
		this.sendField.setEnabled(true);
		this.sendButton.setEnabled(true);
		this.playerList.setEnabled(true);
	}

	@Override
	public void onPlayerConnect(final ServerToClient_PlayerConnectedEvent event)
	{

	}

	@Override
	public void onPlayerDisconnect(final ServerToClient_PlayerDisconnectedEvent event)
	{

	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{
		this.client.getServer().removeListener(this);
		dispose();
		new ClientUI(this.client).setVisible(true);
	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{
		this.statusLabel.setText("Desynchronization error: game already started.");
	}

	@Override
	public void onMessage(final ServerToClient_MessageEvent event)
	{
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
}
