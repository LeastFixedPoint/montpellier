package info.reflectionsofmind.connexion.launcher;

import info.reflecitonsofmind.connexion.platform.gui.ConnexionGUI;
import info.reflecitonsofmind.connexion.platform.gui.connect.ServerInfo;
import info.reflectionsofmind.connexion.game.fortress.gui.host.FortressHostGui;
import info.reflectionsofmind.connexion.game.fortress.gui.join.FortressJoinGui;
import info.reflectionsofmind.connexion.transport.dummy.DummyTransportFactory;
import info.reflectionsofmind.connexion.transport.dummy.gui.AddDummyTransportWizard;
import info.reflectionsofmind.connexion.transport.jabber.JabberConnectionParameters;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransportFactory;
import info.reflectionsofmind.connexion.transport.jabber.gui.AddJabberTransportWizard;

public class Launcher
{
	public static void main(final String[] args)
	{
		final ConnexionGUI gui = new ConnexionGUI(null);
		
		final JabberTransportFactory jabberTransportFactory = new JabberTransportFactory();
		final DummyTransportFactory dummyTransportFactory = new DummyTransportFactory();
		
		gui.getAddTransportWizards().put(jabberTransportFactory, new AddJabberTransportWizard());
		gui.getAddTransportWizards().put(dummyTransportFactory, new AddDummyTransportWizard());
		
		gui.getHostGameGuis().add(new FortressHostGui());
		gui.getJoinGameGuis().add(new FortressJoinGui());
		
		gui.getServerList().add(new ServerInfo("Connexion-server", new JabberTransportFactory(), //
				new JabberConnectionParameters("connexion", "connexion", "binaryfreedom.info", "connexion", 5222), // 
				"connexion-server@binaryfreedom.info"));
		
		gui.start();
	}
}
