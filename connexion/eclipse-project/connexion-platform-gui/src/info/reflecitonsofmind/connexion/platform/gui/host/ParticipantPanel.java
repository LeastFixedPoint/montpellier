package info.reflecitonsofmind.connexion.platform.gui.host;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ParticipantPanel extends JPanel
{
	private final Participant participant;
	private final ActiveTransportPanel activeTransportPanel;
	
	public ParticipantPanel(final ActiveTransportPanel activeTransportPanel, final Participant participant)
	{
		this.activeTransportPanel = activeTransportPanel;
		this.participant = participant;
		setLayout(new MigLayout("ins 0", "[18][max]", "[]"));
		add(new JButton(new KickParticipantAction()));
		add(new JLabel(participant.getName()));
	}
	
	public Participant getParticipant()
	{
		return this.participant;
	}
	
	private final class KickParticipantAction extends AbstractAction
	{
		public KickParticipantAction()
		{
			super("Kick");
		}
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			ParticipantPanel.this.activeTransportPanel.getActiveTransportsPanel().getHostGameFrame().disconnect(
					ParticipantPanel.this.participant);
		}
	}
}
