package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.slot.ISlot.State;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class TransportCombo extends JComboBox implements ISlot.IServerToClientEventListener
{
	public TransportCombo(SlotPanel panel)
	{
		final List<ClientType> clientTypes = new ArrayList<ClientType>();
		clientTypes.add(ClientType.NONE);
		for (final ITransport availableTransport : panel.getPanel().getHostGameDialog().getServer().getTransports())
		{
			clientTypes.add(new ClientType(availableTransport));
		}

		setModel(new DefaultComboBoxModel(clientTypes.toArray()));
		addItemListener(panel);
	}
	
	@Override
	public void onAfterSlotStateChange(ISlot slot, State previousState)
	{
		setEnabled(slot.getState() == ISlot.State.CLOSED);
	}
}
