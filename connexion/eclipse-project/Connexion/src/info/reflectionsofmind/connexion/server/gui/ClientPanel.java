package info.reflectionsofmind.connexion.server.gui;

import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.server.remote.ClientConnectionException;
import info.reflectionsofmind.connexion.server.remote.ClientType;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;
import info.reflectionsofmind.connexion.server.remote.connector.IClientConnector;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.ImmutableMap;

class ClientPanel extends JPanel implements IClientConnector.IListener
{
	private static final Map<String, ClientType> CLIENT_TYPES = // 
	new ImmutableMap.Builder<String, ClientType>() //
			.put("", ClientType.NONE) //
			.put("Local", ClientType.LOCAL) //
			.put("Bot", ClientType.BOT) //
			.put("Jabber", ClientType.JABBER) //
			.build();

	private final ServerUI serverUI;

	private final JComboBox clientTypeCombo;
	private final JLabel statusLabel;

	private IClientConnector connector;
	private IRemoteClient client;

	public ClientPanel(final ServerUI serverUI, final int index)
	{
		this.serverUI = serverUI;
		setLayout(new MigLayout("ins 0", "[18]6[120]6[240]", "[24]"));

		this.clientTypeCombo = new JComboBox(CLIENT_TYPES.keySet().toArray());

		this.statusLabel = new JLabel("");

		add(new JLabel("#" + (index + 1)), "grow");
		add(this.clientTypeCombo, "grow");
		add(this.statusLabel, "grow");

		if (index == 0)
		{
			this.clientTypeCombo.setSelectedIndex(1);
		}

		this.serverUI.pack();
	}

	public void onDisconnect(final DisconnectReason reason)
	{
		this.client = null;
		this.statusLabel.setText("Disconnected.");

		if (this.serverUI.getServer().getGame() == null)
		{
			this.clientTypeCombo.setEnabled(true);
		}
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}

	public ClientType getClientType()
	{
		return CLIENT_TYPES.get(this.clientTypeCombo.getSelectedItem());
	}

	@Override
	public void onConnected(final IRemoteClient client)
	{
		this.client = client;
		this.serverUI.onClientConnected(this);
	}

	public synchronized void startListening()
	{
		this.clientTypeCombo.setEnabled(false);

		final ClientType clientType = CLIENT_TYPES.get(this.clientTypeCombo.getSelectedItem());
		this.connector = clientType.getConnector(this.serverUI.getServer());
		this.connector.addListener(this);
		this.connector.listen();
	}

	public synchronized void cancelListening()
	{
		if (this.client != null)
		{
			try
			{
				this.client.disconnect(DisconnectReason.SERVER_SHUTDOWN);
				this.client = null;
			}
			catch (final ClientConnectionException exception)
			{
				exception.printStackTrace();
			}
		}
		else
		{
			this.connector.disconnect();
		}

		this.connector = null;
		this.clientTypeCombo.setEnabled(true);
	}
}