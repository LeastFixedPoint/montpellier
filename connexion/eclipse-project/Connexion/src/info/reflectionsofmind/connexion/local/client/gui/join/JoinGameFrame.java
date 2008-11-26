package info.reflectionsofmind.connexion.local.client.gui.join;

import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.local.client.gui.play.ClientUI;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteJabberServer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jivesoftware.smack.ConnectionConfiguration;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JFrame implements IRemoteServer.IListener
{
	private final JLabel statusLabel;
	private final List<JLabel> playerLabels = new ArrayList<JLabel>();
	private final JComboBox connectionTypeCombo;
	private final JButton connectButton;

	private IClient client;
	
	public JoinGameFrame()
	{
		super("Connexion :: Join game");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[120]6[120]6[240]", "[]"));

		this.connectionTypeCombo = new JComboBox();
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

		this.statusLabel = new JLabel("");
		add(this.statusLabel, "grow, wrap");

		pack();
		setLocationRelativeTo(null);
	}

	public void connect()
	{
		/*
		this.client = new DefaultLocalClient("Test");
	
		final ConnectionConfiguration clientConfig = new ConnectionConfiguration("binaryfreedom.info");
		clientConfig.setSASLAuthenticationEnabled(false);
		final String serverAddress = "shooshpanchick@gmail.com";
		final IRemoteServer server = new RemoteJabberServer(clientConfig, serverAddress);
		
		this.client.connect(server);*/
		
		final JabberConfigurationDialog dialog = new JabberConfigurationDialog(this);
		dialog.setVisible(true);
	}

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		this.statusLabel.setText("Connected");
	}

	@Override
	public void onPlayerConnect(final ServerToClient_PlayerConnectedEvent event)
	{
		final JLabel playerLabel = new JLabel(event.getPlayerName());
		this.playerLabels.add(playerLabel);
		add(playerLabel, "cell 0 1");
	}

	@Override
	public void onPlayerDisconnect(final ServerToClient_PlayerDisconnectedEvent event)
	{
		remove(this.playerLabels.get(event.getPlayerIndex()));
		this.playerLabels.remove(event.getPlayerIndex());
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
}
