package info.reflectionsofmind.connexion.launcher;

import info.reflecitonsofmind.connexion.platform.gui.ConnexionGUI;
import info.reflectionsofmind.connexion.transport.dummy.DummyTransportFactory;
import info.reflectionsofmind.connexion.transport.dummy.gui.AddDummyTransportWizard;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransportFactory;
import info.reflectionsofmind.connexion.transport.jabber.gui.AddJabberTransportWizard;

public class Connexion
{
	public static void main(final String[] args)
	{
		final ConnexionGUI gui = new ConnexionGUI(null);
		
		final JabberTransportFactory jabberTransportFactory = new JabberTransportFactory();
		final DummyTransportFactory dummyTransportFactory = new DummyTransportFactory();
		
		gui.getAddTransportWizards().put(jabberTransportFactory, new AddJabberTransportWizard());
		gui.getAddTransportWizards().put(dummyTransportFactory, new AddDummyTransportWizard());
		
		gui.start();
	}
}
