package info.reflectionsofmind.connexion.server.gui;

import info.reflectionsofmind.connexion.client.ConnectionFailedException;
import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

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

	private IRemoteClient client;

	public ClientPanel(final ServerUI serverUI, final int index)
	{
		this.serverUI = serverUI;
		setLayout(new MigLayout("ins 0", "[18]6[120]6[120]6[240]", "[24]"));

		this.clientTypeCombo = new JComboBox(ServerUI.CLIENT_TYPES.keySet().toArray());
		this.clientTypeCombo.addItemListener(this);

		this.connectButton = new JButton(new ConnectAction());
		this.connectButton.setEnabled(false);

		this.statusLabel = new JLabel("");

		add(new JLabel("#" + (index + 1)), "grow");
		add(this.clientTypeCombo, "grow");
		add(this.connectButton, "grow");
		add(this.statusLabel, "grow");

		if (index == 0)
		{
			this.clientTypeCombo.setSelectedIndex(1);
		}

		this.serverUI.pack();
	}

	private void connect()
	{
		final ClientType clientType = ServerUI.CLIENT_TYPES.get(this.clientTypeCombo.getSelectedItem());

		this.clientTypeCombo.setEnabled(false);
		this.connectButton.setEnabled(false);
		this.statusLabel.setText("Connecting...");

		try
		{
			this.client = clientType.connect(this.serverUI.getServer());
			this.serverUI.getServer().add(getClient());
			this.statusLabel.setText("Connected as [" + getClient().getName() + "].");
		}
		catch (final ConnectionFailedException exception)
		{
			JOptionPane.showMessageDialog(this, "Connection failed. Reason:\n" + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			this.statusLabel.setText("Connection failed.");
			this.clientTypeCombo.setEnabled(true);
			this.connectButton.setEnabled(true);
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

	public void onDisconnect(final DisconnectReason reason)
	{
		this.client = null;
		this.statusLabel.setText("Disconnected.");

		if (this.serverUI.getServer().getGame() == null)
		{
			this.connectButton.setAction(new ConnectAction());
			this.clientTypeCombo.setEnabled(true);
		}
	}

	@Override
	public void disable()
	{
		this.connectButton.setEnabled(false);
		this.clientTypeCombo.setEnabled(false);
	}

	public IRemoteClient getClient()
	{
		return this.client;
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