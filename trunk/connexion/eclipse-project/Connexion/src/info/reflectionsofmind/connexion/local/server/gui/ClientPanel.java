package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.slot.Slot;
import info.reflectionsofmind.connexion.local.server.slot.ISlot.State;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jvnet.substance.SubstanceLookAndFeel;

class ClientPanel extends JPanel implements ISlot.IListener
{
	private static final long serialVersionUID = 1L;

	private final ClientsPanel panel;

	private final JComboBox clientTypeCombo;
	private final JLabel statusLabel;
	private final JButton listenButton;
	private final JButton removeButton;

	private ISlot<?> slot;

	public ClientPanel(final ClientsPanel panel)
	{
		this.panel = panel;
		setLayout(new MigLayout("ins 0", "[120]6[120]6[240]", "[24]"));

		this.removeButton = new JButton(new RemoveAction());
		this.removeButton.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, Boolean.TRUE);

		final List<ClientType> clientTypes = new ArrayList<ClientType>();
		clientTypes.add(new ClientType(null));
		for (final ITransport<?> transport : panel.getWindow().getServer().getTransports())
		{
			clientTypes.add(new ClientType(transport));
		}

		this.clientTypeCombo = new JComboBox(clientTypes.toArray());
		add(this.clientTypeCombo, "grow");

		this.listenButton = new JButton(new ListenAction());
		add(this.listenButton, "grow");

		this.statusLabel = new JLabel("");
		add(this.statusLabel, "grow");
	}

	@Override
	public void onAfterSlotStateChange(final State previousState)
	{
		switch (getSlot().getState())
		{
			case CLOSED:
				this.statusLabel.setText("");
				break;
			case OPEN:
				this.statusLabel.setText("Listening...");
				break;
			case CONNECTED:
				this.statusLabel.setText("Client [" + getSlot().getClient().getName() + "] connected.");
				break;
			case ACCEPTED:
				this.statusLabel.setText("Player [" + getSlot().getPlayer().getName() + "] accepted into the game.");
				break;
			case ERROR:
				this.statusLabel.setText("Error :" + getSlot().getError().getLocalizedMessage() + ".");
				break;
		}
	}

	@SuppressWarnings("unchecked")
	public void listen()
	{
		this.listenButton.setAction(new CancelAction());
		this.clientTypeCombo.setEnabled(false);
		this.removeButton.setEnabled(false);

		final ITransport<INode> transport = (ITransport<INode>) ((ClientType) this.clientTypeCombo.getSelectedItem()).getTransport();

		this.slot = new Slot<INode, ITransport<INode>>(this.panel.getWindow().getServer(), transport);
		this.slot.addListener(this);
		this.slot.open();
	}

	public void cancel()
	{
		this.slot.close();

		this.listenButton.setAction(new ListenAction());
		this.clientTypeCombo.setEnabled(true);
		this.removeButton.setEnabled(true);
		this.statusLabel.setText("Cancelled.");
	}

	public void disconnect()
	{
		this.slot.disconnect(DisconnectReason.SERVER_REQUEST);

		this.listenButton.setAction(new ListenAction());
		this.clientTypeCombo.setEnabled(true);
		this.removeButton.setEnabled(true);
		this.statusLabel.setText("Disconnected.");
	}

	public void fade()
	{
		this.listenButton.setEnabled(false);
		this.clientTypeCombo.setEnabled(false);
		this.removeButton.setEnabled(false);
	}

	public ISlot<?> getSlot()
	{
		return this.slot;
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
			ClientPanel.this.panel.removePanel(ClientPanel.this);
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
}