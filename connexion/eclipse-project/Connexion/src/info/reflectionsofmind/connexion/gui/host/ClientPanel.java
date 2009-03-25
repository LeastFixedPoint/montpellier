package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.server.IRemoteClient;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.ImmutableMap;

class ClientPanel extends JPanel implements Participant.IStateListener
{
	private static final long serialVersionUID = 1L;

	private final ClientsPanel panel;

	private final Action acceptAction = new AcceptAction();
	private final Action spectateAction = new SpectateAction();
	private final Action rejectAction = new RejectAction();
	private final Action kickAction = new KickAction();

	private final IRemoteClient client;

	private final Map<State, ButtonPanel> buttonPanels = ImmutableMap.<State, ButtonPanel> builder() //
			.put(State.CONNECTED, new ButtonPanel("[grow][grow][grow]", this.acceptAction, this.spectateAction, this.kickAction)) //
			.put(State.ACCEPTED, new ButtonPanel("[grow][grow]", this.rejectAction, this.kickAction)) //
			.put(State.SPECTATOR, new ButtonPanel("[grow][grow]", this.rejectAction, this.kickAction)) //
			.build();

	private final JLabel nameLabel;
	private ButtonPanel currentButtonPanel = this.buttonPanels.get(State.CONNECTED);

	public ClientPanel(final ClientsPanel panel, final IRemoteClient client)
	{
		this.client = client;
		this.panel = panel;

		setLayout(new MigLayout("ins 0 6 6 6", "[60][120]", "[]"));

		this.nameLabel = new JLabel(client.getParticipant().getName());

		add(this.nameLabel, "grow, wrap");
		add(this.currentButtonPanel, "grow, span");

		this.client.getParticipant().addStateListener(this);
	}

	@Override
	public void onAfterClientStateChange(Participant client, State previousState)
	{
		this.remove(this.currentButtonPanel);
		this.currentButtonPanel = this.buttonPanels.get(client.getState());
		this.add(this.currentButtonPanel, "grow, span");
		getLayout().layoutContainer(this);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		this.currentButtonPanel.setEnabled(false);
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public IRemoteClient getClient()
	{
		return this.client;
	}

	public ClientsPanel getPanel()
	{
		return this.panel;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

	public class AcceptAction extends AbstractAction
	{
		public AcceptAction()
		{
			super("Accept");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			getClient().getParticipant().setAccepted();
		}
	}

	public class SpectateAction extends AbstractAction
	{
		public SpectateAction()
		{
			super("Spectate");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			getClient().getParticipant().setSpectator();
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
			getClient().getParticipant().setRejected();
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
			panel.getHostGameDialog().getServer().disconnect(getClient(), DisconnectReason.SERVER_REQUEST);
		}
	}

	private class ButtonPanel extends JPanel
	{
		private final List<JButton> buttons = new ArrayList<JButton>();

		public ButtonPanel(final String rowLayout, final Action... actions)
		{
			setLayout(new MigLayout("ins 0", rowLayout, "[]"));

			for (final Action action : actions)
			{
				final JButton button = new JButton(action);
				add(button, "grow");
				this.buttons.add(button);
			}
		}

		@Override
		public void setEnabled(boolean enabled)
		{
			for (JButton button : this.buttons)
				button.setEnabled(enabled);
		}
	}
}