package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.remote.client.ClientType;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.connector.IClientConnector;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jvnet.substance.SubstanceLookAndFeel;

import com.google.common.collect.ImmutableMap;

class ClientPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static final Map<String, ClientType> CLIENT_TYPES = // 
	new ImmutableMap.Builder<String, ClientType>() //
			.put("Local", ClientType.LOCAL) //
			.put("Bot", ClientType.BOT) //
			.put("Jabber", ClientType.JABBER) //
			.build();

	private final ServerUI serverUI;

	private final JComboBox clientTypeCombo;
	private final JLabel statusLabel;
	private final JButton listenButton;
	private final JButton removeButton;

	private State state = State.WAITING;
	private IRemoteClient client;
	private IClientConnector connector;

	public ClientPanel(final ServerUI serverUI)
	{
		this.serverUI = serverUI;
		setLayout(new MigLayout("ins 0", "[24][120]6[120]6[240]", "[24]"));

		this.removeButton = new JButton(new RemoveAction());
		this.removeButton.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, Boolean.TRUE);
		add(this.removeButton, "grow");

		this.clientTypeCombo = new JComboBox(CLIENT_TYPES.keySet().toArray());
		add(this.clientTypeCombo, "grow");

		this.listenButton = new JButton(new ListenAction());
		add(this.listenButton, "grow");

		this.statusLabel = new JLabel("");
		add(this.statusLabel, "grow");

		this.serverUI.pack();
	}

	public void listen()
	{
		if (this.state != State.WAITING) throw new IllegalStateException();

		this.listenButton.setAction(new CancelAction());
		this.clientTypeCombo.setEnabled(false);
		this.removeButton.setEnabled(false);

		final ClientType clientType = CLIENT_TYPES.get(this.clientTypeCombo.getSelectedItem());

		final IClientConnector connector = clientType.getConnector(this.serverUI.getServer());
		connector.addListener(new IClientConnector.IListener()
		{
			@Override
			public void onConnected(final IRemoteClient client)
			{
				ClientPanel.this.onConnected(client);
			}
		});

		this.state = State.LISTENING;
		connector.startListening();
	}

	private void onConnected(final IRemoteClient client)
	{
		this.connector = null;
		this.client = client;
		this.listenButton.setAction(new DisconnectAction());
		this.statusLabel.setText("Connected as [" + client.getName() + "].");
		this.state = State.CONNECTED;

		this.serverUI.onClientConnected(ClientPanel.this);
	}

	public void cancel()
	{
		if (this.state != State.LISTENING) throw new IllegalStateException();

		this.connector.stopListening();

		this.connector = null;
		this.client = null;
		this.listenButton.setAction(new ListenAction());
		this.clientTypeCombo.setEnabled(true);
		this.removeButton.setEnabled(true);
		this.statusLabel.setText("Cancelled.");
		this.state = State.WAITING;
	}

	public void disconnect()
	{
		if (this.state != State.CONNECTED) throw new IllegalStateException();

		this.connector = null;
		this.client = null;
		this.state = State.WAITING;
		this.listenButton.setAction(new ListenAction());
		this.clientTypeCombo.setEnabled(true);
		this.removeButton.setEnabled(true);
		this.statusLabel.setText("Disconnected.");

		this.serverUI.onClientDisconnected(this);
	}

	public void fade()
	{
		this.listenButton.setEnabled(false);
		this.clientTypeCombo.setEnabled(false);
		this.removeButton.setEnabled(false);
	}	

	public State getState()
	{
		return this.state;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

	private class RemoveAction extends AbstractAction
	{
		private RemoveAction()
		{
			super("X");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			ClientPanel.this.serverUI.removeClientPanel(ClientPanel.this);
		}
	}

	public class ListenAction extends AbstractAction
	{
		public ListenAction()
		{
			super("Listen");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			listen();
		}
	}

	public class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super("Cancel");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			cancel();
		}
	}

	public class DisconnectAction extends AbstractAction
	{
		public DisconnectAction()
		{
			super("Disconnect");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			disconnect();
		}
	}

	// ====================================================================================================
	// === STATE
	// ====================================================================================================

	public enum State
	{
		WAITING, LISTENING, CONNECTED
	}
}