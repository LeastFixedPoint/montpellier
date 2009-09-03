package info.reflecitonsofmind.connexion.platform.gui.host;

import static javax.swing.BorderFactory.createTitledBorder;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.util.Util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class ActiveTransportPanel extends JPanel
{
	private final ITransport transport;
	private final List<ParticipantPanel> panels = new ArrayList<ParticipantPanel>();
	private final ActiveTransportsPanel activeTransportsPanel;
	
	public ActiveTransportPanel(final ActiveTransportsPanel activeTransportsPanel, final ITransport transport)
	{
		this.activeTransportsPanel = activeTransportsPanel;
		this.transport = transport;
		
		setLayout(new MigLayout("", "[max, fill]", getRowConstraints()));
		
		setBorder(createTitledBorder(transport.getName()));
		add(new JButton(new ShutdownTransportAction()), "wrap");
	}
	
	public ActiveTransportsPanel getActiveTransportsPanel()
	{
		return this.activeTransportsPanel;
	}
	
	@Override
	public MigLayout getLayout()
	{
		return (MigLayout) super.getLayout();
	}
	
	private String getRowConstraints()
	{
		return "[24]" + Util.copy(this.panels.size(), "[]");
	}
	
	public void addParticipant(final Participant participant)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				final ParticipantPanel panel = new ParticipantPanel(ActiveTransportPanel.this, participant);
				ActiveTransportPanel.this.panels.add(panel);
				getLayout().setRowConstraints(getRowConstraints());
				add(panel, "wrap");
				updateUI();
			}
		});
	}
	
	public void removeParticipant(final Participant participant)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				final ParticipantPanel panel = getPanelByParticipant(participant);
				ActiveTransportPanel.this.panels.remove(panel);
				remove(panel);
				getLayout().setRowConstraints(getRowConstraints());
				updateUI();
			}
		});
	}
	
	public ParticipantPanel getPanelByParticipant(final Participant participant)
	{
		for (final ParticipantPanel panel : this.panels)
			if (panel.getParticipant() == participant) return panel;
		return null;
	}
	
	public ITransport getTransport()
	{
		return this.transport;
	}
	
	private final class ShutdownTransportAction extends AbstractAction
	{
		public ShutdownTransportAction()
		{
			super("Shut down");
		}
		
		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
			for (final ParticipantPanel panel : ActiveTransportPanel.this.panels)
				getActiveTransportsPanel().getHostGameFrame().disconnect(panel.getParticipant());
			
			ActiveTransportPanel.this.transport.stop();
		}
	}
}
