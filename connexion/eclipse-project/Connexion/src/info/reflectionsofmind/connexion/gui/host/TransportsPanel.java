package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;

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

		for (IServerTransportFactory transportFactory : getHostGameFrame().getApplication().getServerTransportFactories())
		{
			add(new TransportPanel(this, transportFactory), "grow, span");
		}
	}

	public HostGameFrame getHostGameFrame()
	{
		return this.hostGameFrame;
	}
}
