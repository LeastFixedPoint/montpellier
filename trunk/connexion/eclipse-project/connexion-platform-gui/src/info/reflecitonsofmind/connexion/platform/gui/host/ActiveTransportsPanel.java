package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.util.Util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class ActiveTransportsPanel extends JPanel
{
	private final ITransport.IListener transportListener = new TransportListener();
	
	private final HostGameFrame hostGameFrame;
	private final List<ActiveTransportPanel> panels = new ArrayList<ActiveTransportPanel>();
	
	public ActiveTransportsPanel(final HostGameFrame hostGameFrame)
	{
		this.hostGameFrame = hostGameFrame;
		setBorder(BorderFactory.createTitledBorder("Active transports"));
		setLayout(new MigLayout("", "[max, fill]", getRowConstraints()));
	}
	
	public HostGameFrame getHostGameFrame()
	{
		return this.hostGameFrame;
	}
	
	@Override
	public MigLayout getLayout()
	{
		return (MigLayout) super.getLayout();
	}
	
	private String getRowConstraints()
	{
		return Util.copy(this.panels.size(), "[]") + "[max]";
	}
	
	public ActiveTransportPanel getPanelByTransport(final ITransport transport)
	{
		for (final ActiveTransportPanel panel : ActiveTransportsPanel.this.panels)
			if (panel.getTransport() == transport) return panel;
		return null;
	}
	
	public final class TransportListener extends AbstractTransport.Listener
	{
		@Override
		public void onStarted(final ITransport transport)
		{
			final ActiveTransportPanel panelToAdd = new ActiveTransportPanel(ActiveTransportsPanel.this, transport);
			ActiveTransportsPanel.this.panels.add(panelToAdd);
			
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					getLayout().setRowConstraints(getRowConstraints());
					add(panelToAdd, "wrap");
					updateUI();
				}
			});
		}
		
		@Override
		public void onStopped(final ITransport transport)
		{
			final ActiveTransportPanel panelToRemove = getPanelByTransport(transport);
			ActiveTransportsPanel.this.panels.remove(panelToRemove);
			
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					remove(panelToRemove);
					getLayout().setRowConstraints(getRowConstraints());
					updateUI();
				}
			});
		}
	}
	
	public void addParticipant(final Participant participant)
	{
		getPanelByTransport(participant.getNode().getTransport()).addParticipant(participant);
	}
	
	public void removeParticipant(final Participant participant)
	{
		getPanelByTransport(participant.getNode().getTransport()).removeParticipant(participant);
	}
	
	public ITransport.IListener getTransportListener()
	{
		return this.transportListener;
	}
}
