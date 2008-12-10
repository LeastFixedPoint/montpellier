package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.slot.ISlot.State;
import info.reflectionsofmind.connexion.util.Colors;

import javax.swing.Icon;
import javax.swing.JLabel;

public class StatusLabel extends JLabel implements ISlot.IServerToClientEventListener
{
	private static final int ICON_SIZE = 12;

	private final static Icon CLOSED_ICON = Colors.getEmptyIcon(ICON_SIZE);
	private final static Icon OPEN_ICON = Colors.getEmptyIcon(ICON_SIZE);
	private final static Icon CONNECTED_ICON = Colors.getEmptyIcon(ICON_SIZE);
	private final static Icon ERROR_ICON = Colors.getEmptyIcon(ICON_SIZE);

	private final SlotPanel panel;

	public StatusLabel(SlotPanel panel)
	{
		super("Closed", CLOSED_ICON, JLabel.LEFT);
		this.panel = panel;
	}

	@Override
	public void onAfterSlotStateChange(final ISlot slot, final State previousState)
	{
		switch (slot.getState())
		{
			case CLOSED:
				setText("Closed");
				setIcon(CLOSED_ICON);
				break;

			case OPEN:
				setText("Open");
				setIcon(OPEN_ICON);
				break;

			case CONNECTED:
				setText(this.panel.getSlot().getClient().getName());
				setIcon(CONNECTED_ICON);
				break;

			case ACCEPTED:
				setText(this.panel.getSlot().getPlayer().getName());
				setIcon(Colors.getIcon(this.panel.getIndex(), ICON_SIZE));
				break;

			case ERROR:
				setText("Error");
				setIcon(ERROR_ICON);
				break;
		}
	}
}
