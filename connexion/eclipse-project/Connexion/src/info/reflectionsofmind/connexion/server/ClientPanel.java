package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.client.ClientType;
import info.reflectionsofmind.connexion.client.ConnectionFailedException;
import info.reflectionsofmind.connexion.client.IClient;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

class ClientPanel extends JPanel implements ItemListener
{
	private final ServerUI serverUI;

	private static final long serialVersionUID = 1L;

	private final JComboBox clientTypeCombo;
	private final JButton connectButton;
	private final JLabel statusLabel;

	private IClient client;

	public ClientPanel(final ServerUI serverUI, final int index)
	{
		this.serverUI = serverUI;
		setLayout(new MigLayout("ins 0", "[18]6[120]6[120]6[240]", "[24]"));

		this.clientTypeCombo = new JComboBox(ServerUI.CLIENT_TYPES.keySet().toArray());
		this.clientTypeCombo.addItemListener(this);

		this.connectButton = new JButton(new ConnectAction());
		this.connectButton.setEnabled(false);

		this.statusLabel = new JLabel("");

		add(new JLabel("#" + index), "grow");
		add(this.clientTypeCombo, "grow");
		add(this.connectButton, "grow");
		add(this.statusLabel, "grow");

		this.serverUI.pack();
	}

	private void connect()
	{
		final ClientType clientType = ServerUI.CLIENT_TYPES.get(this.clientTypeCombo.getSelectedItem());

		this.connectButton.setAction(new CancelAction());
		this.clientTypeCombo.setEnabled(false);
		this.statusLabel.setText("Connecting...");

		try
		{
			this.client = clientType.connect(this.serverUI.getServer());
			this.statusLabel.setText("Connected as [" + this.client.getName() + "].");
			this.connectButton.setAction(new DisconnectAction());
		}
		catch (final ConnectionFailedException exception)
		{
			JOptionPane.showMessageDialog(this, "Connection failed. Reason:\n" + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			this.statusLabel.setText("Connection failed.");
			this.connectButton.setAction(new ConnectAction());
			this.clientTypeCombo.setEnabled(true);
		}

		this.serverUI.updateStartButton();
	}

	@Override
	public void itemStateChanged(final ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			this.connectButton.setEnabled(e.getItem() != ServerUI.CLIENT_TYPES.keySet().iterator().next());
		}
	}

	public void onDisconnect(DisconnectReason reason)
	{
		this.client = null;
		this.statusLabel.setText("Disconnected.");
		
		if (this.serverUI.getServer().getGame() == null)
		{
			this.connectButton.setAction(new ConnectAction());
			this.clientTypeCombo.setEnabled(true);
		}
	}
	
	public void disable()
	{
		this.connectButton.setEnabled(false);
		this.clientTypeCombo.setEnabled(false);
	}

	public IClient getClient()
	{
		return this.client;
	}

	private class DisconnectAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public DisconnectAction()
		{
			super("Disconnect");
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			ClientPanel.this.serverUI.getServer().disconnect(getClient(), DisconnectReason.SERVER_REQUEST);
			onDisconnect(DisconnectReason.SERVER_REQUEST);
		}
	}

	private class CancelAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public CancelAction()
		{
			super("Cancel");
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			ClientPanel.this.serverUI.getServer().disconnect(getClient(), DisconnectReason.SERVER_REQUEST);
			onDisconnect(DisconnectReason.SERVER_REQUEST);
		}
	}

	private class ConnectAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public ConnectAction()
		{
			super("Connect");
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			connect();
		}
	}
}