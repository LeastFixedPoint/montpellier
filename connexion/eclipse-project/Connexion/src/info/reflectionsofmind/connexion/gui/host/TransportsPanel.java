package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.transport.ITransport;

import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class TransportsPanel extends JPanel
{
	private final HostGameFrame hostGameFrame;

	public TransportsPanel(HostGameFrame hostGameFrame) throws HeadlessException
	{
		this.hostGameFrame = hostGameFrame;
		
		setLayout(new MigLayout("ins 0 6 6 6", "[]", ""));
		setBorder(BorderFactory.createTitledBorder("Transports"));
		
		for (ITransport transport : getHostGameFrame().getServer().getTransports())
		{
			add(new TransportPanel(this, transport), "grow, span");
		}
	}

	public HostGameFrame getHostGameFrame()
	{
		return this.hostGameFrame;
	}
}
