package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.slot.Slot;
import info.reflectionsofmind.connexion.local.server.slot.ISlot.State;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.util.Colors;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

class ClientPanel extends JPanel implements ISlot.IListener, ItemListener
{
	private static final long serialVersionUID = 1L;

	private final ClientsPanel panel;

	private final JLabel statusLabel;
	private final JComboBox clientTypeCombo;
	private final JButton listenButton;
	private final Icon icon;

	private ISlot<?,?> slot;

	public ClientPanel(final ClientsPanel panel, final int index)
	{
		this.panel = panel;
		this.icon = Colors.getIcon(index, 12);

		setLayout(new MigLayout("ins 0 6 6 6", "[120]", "[24][24][24]"));
		setBorder(BorderFactory.createTitledBorder("Player " + (index + 1)));

		final List<ClientType> clientTypes = new ArrayList<ClientType>();
		clientTypes.add(new ClientType(null));
		for (final ITransport<?> transport : panel.getWindow().getServer().getTransports())
		{
			clientTypes.add(new ClientType(transport));
		}

		this.statusLabel = new JLabel("Not connected", Colors.getEmptyIcon(12), SwingConstants.LEFT);
		this.listenButton = new JButton(new ListenAction());
		this.listenButton.setEnabled(index == 0);

		this.clientTypeCombo = new JComboBox(clientTypes.toArray());
		this.clientTypeCombo.addItemListener(this);
		this.clientTypeCombo.setSelectedIndex((index == 0) ? 1 : 0);

		add(this.statusLabel, "grow, wrap");
		add(this.clientTypeCombo, "grow, span");
		add(this.listenButton, "grow, span");
	}

	@Override
	public void onAfterSlotStateChange(final State previousState)
	{
		switch (getSlot().getState())
		{
			case CLOSED:
				this.statusLabel.setIcon(Colors.getEmptyIcon(12));
				this.statusLabel.setText("Not connected");
				break;
			case OPEN:
				this.statusLabel.setIcon(Colors.getEmptyIcon(12));
				this.statusLabel.setText("Listening");
				break;
			case CONNECTED:
				this.statusLabel.setIcon(this.icon);
				this.statusLabel.setText(getSlot().getClient().getName());
				break;
			case ACCEPTED:
				this.statusLabel.setIcon(this.icon);
				this.statusLabel.setText(getSlot().getPlayer().getName());
				break;
			case ERROR:
				this.statusLabel.setIcon(Colors.getEmptyIcon(12));
				this.statusLabel.setText("Error");
				break;
		}
	}

	@SuppressWarnings("unchecked")
	public void listen()
	{
		this.listenButton.setAction(new CancelAction());
		this.clientTypeCombo.setEnabled(false);

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
		this.statusLabel.setText("Cancelled.");
	}

	public void disconnect()
	{
		this.slot.disconnect(DisconnectReason.SERVER_REQUEST);

		this.listenButton.setAction(new ListenAction());
		this.clientTypeCombo.setEnabled(true);
		this.statusLabel.setText("Disconnected.");
	}

	public void fade()
	{
		this.listenButton.setEnabled(false);
		this.clientTypeCombo.setEnabled(false);
	}

	public ISlot<?,?> getSlot()
	{
		return this.slot;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

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

	@Override
	public void itemStateChanged(ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			this.listenButton.setEnabled(this.clientTypeCombo.getSelectedIndex() > 0);
		}
	}
}