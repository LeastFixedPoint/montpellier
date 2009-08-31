package info.reflectionsofmind.connexion.transport.jabber.gui;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflecitonsofmind.connexion.platform.gui.host.IAddTransportWizard;
import info.reflectionsofmind.connexion.transport.jabber.JabberConnectionParameters;

public class AddJabberTransportWizard implements IAddTransportWizard
{
	public JabberConnectionParameters execute(final HostGameFrame hostGameFrame)
	{
		return new JabberConnectionParameters("connexion", "connexion", "binaryfreedom.info", null, null);
	}
}
