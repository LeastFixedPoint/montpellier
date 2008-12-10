package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.gui.MainFrame;
import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.slot.Slot;
import info.reflectionsofmind.connexion.local.server.slot.ISlot.State;
import info.reflectionsofmind.connexion.remote.server.LocalRemoteServer;
import info.reflectionsofmind.connexion.transport.local.ServerLocalTransport;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.ImmutableMap;

class SlotPanel extends JPanel implements IServerToClientEventListener.IListener, ItemListener
{
	private static final long serialVersionUID = 1L;

	private final ClientsPanel panel;

	private final int index;

	private final Action listenAction = new ListenAction();
	private final Action cancelAction = new CancelAction();
	private final Action acceptAction = new AcceptAction();
	private final Action rejectAction = new RejectAction();
	private final Action kickAction = new KickAction();

	private final ISlot slot;

	private final Map<ISlot.State, ButtonPanel> buttonPanels = ImmutableMap.<ISlot.State, ButtonPanel> builder() //
			.put(ISlot.State.CLOSED, new ButtonPanel("[grow]", this.listenAction)) // 
			.put(ISlot.State.OPEN, new ButtonPanel("[grow]", this.cancelAction)) //
			.put(ISlot.State.CONNECTED, new ButtonPanel("[grow 2][grow 1]", this.acceptAction, this.kickAction)) //
			.put(ISlot.State.ACCEPTED, new ButtonPanel("[grow 2][grow 1]", this.rejectAction, this.kickAction)) //
			.put(ISlot.State.ERROR, new ButtonPanel("[grow]", this.listenAction)) //
			.build();

	private ButtonPanel currentButtonPanel = this.buttonPanels.get(ISlot.State.CLOSED);

	private final TransportCombo transportCombo;

	private final StatusLabel statusLabel;

	public SlotPanel(final ClientsPanel panel, final int index)
	{
		this.index = index;
		this.panel = panel;
		
		this.slot = new Slot(panel.getHostGameDialog().getServer());

		setLayout(new MigLayout("ins 0 6 6 6", "[120]", "[24][24][24]"));
		setBorder(BorderFactory.createTitledBorder("Slot " + (index + 1)));

		this.statusLabel = new StatusLabel(this);
		this.transportCombo = new TransportCombo(this);
		this.transportCombo.setSelectedIndex(getIndex() == 0 ? 1 : 0);
		this.listenAction.setEnabled(this.transportCombo.getSelectedIndex() != 0);

		this.slot.addListener(this);
		this.slot.addListener(this.transportCombo);
		this.slot.addListener(this.statusLabel);
		
		updateSlot();
		
		add(this.statusLabel, "grow, wrap");
		add(this.transportCombo, "grow, span");
		add(this.currentButtonPanel, "grow, span");
	}

	@Override
	public void onAfterSlotStateChange(final ISlot slot, final State previousState)
	{
		this.remove(this.currentButtonPanel);
		this.currentButtonPanel = this.buttonPanels.get(slot.getState());
		this.add(this.currentButtonPanel, "grow, span");
		getLayout().layoutContainer(this);

		if (slot.getState() == ISlot.State.OPEN && previousState == ISlot.State.CLOSED)
		{
			final ServerLocalTransport transport = (ServerLocalTransport) slot.getTransport();
			final LocalRemoteServer remoteServer = transport.getRemoteServers().get(transport.getRemoteServers().size() - 1);

			final MainFrame mainWindow = this.panel.getHostGameDialog().getMainWindow();
			final JoinGameFrame frame = new JoinGameFrame(mainWindow, remoteServer);
			frame.setVisible(true);
			frame.connect();
		}
	}

	public void open()
	{
		getSlot().open();
	}

	public void cancel()
	{
		getSlot().close();
	}

	public void kick()
	{
		getSlot().setDisconnected(DisconnectReason.SERVER_REQUEST);
	}

	public void accept()
	{
		getSlot().setAcceptedAs(new Player(getSlot().getClient().getName()));
	}

	public void reject()
	{
		
	}

	private void updateSlot()
	{
		final ClientType selectedClientType = (ClientType) this.transportCombo.getSelectedItem();
		this.slot.setTransport(selectedClientType.getTransport());
	}

	@Override
	public void itemStateChanged(final ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			updateSlot();
			this.listenAction.setEnabled(event.getItem() != ClientType.NONE);
		}
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public ISlot getSlot()
	{
		return this.slot;
	}

	public ClientsPanel getPanel()
	{
		return this.panel;
	}

	public int getIndex()
	{
		return this.index;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

	public class ListenAction extends AbstractAction
	{
		public ListenAction()
		{
			super("Open");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			open();
		}
	}

	public class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super("Close");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			cancel();
		}
	}

	public class AcceptAction extends AbstractAction
	{
		public AcceptAction()
		{
			super("Accept");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			accept();
		}
	}

	public class RejectAction extends AbstractAction
	{
		public RejectAction()
		{
			super("Reject");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			setRejected();
		}
	}

	public class KickAction extends AbstractAction
	{
		public KickAction()
		{
			super("Kick");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			kick();
		}
	}

	private class ButtonPanel extends JPanel
	{
		public ButtonPanel(final String rowLayout, final Action... actions)
		{
			setLayout(new MigLayout("ins 0", rowLayout, "[]"));

			for (final Action action : actions)
			{
				add(new JButton(action), "grow");
			}
		}
	}
}