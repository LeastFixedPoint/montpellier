package info.reflectionsofmind.connexion.launcher;

import info.reflectionsofmind.connexion.game.fortress.Fortress;
import info.reflectionsofmind.connexion.game.fortress.gui.host.FortressHostGui;
import info.reflectionsofmind.connexion.game.fortress.gui.join.FortressJoinGui;
import info.reflectionsofmind.connexion.platform.gui.ConnexionGUI;
import info.reflectionsofmind.connexion.platform.gui.connect.ServerInfo;
import info.reflectionsofmind.connexion.transport.dummy.DummyTransportFactory;
import info.reflectionsofmind.connexion.transport.dummy.gui.AddDummyTransportWizard;
import info.reflectionsofmind.connexion.transport.hotseat.HotseatTransportFactory;
import info.reflectionsofmind.connexion.transport.hotseat.gui.AddHotseatTransportWizard;
import info.reflectionsofmind.connexion.transport.hotseat.gui.GuiHotSeatClientFactory;
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
		final HotseatTransportFactory hotseatTransportFactory = new HotseatTransportFactory(
				new GuiHotSeatClientFactory(gui));
		
		gui.getAddTransportWizards().put(jabberTransportFactory, new AddJabberTransportWizard());
		gui.getAddTransportWizards().put(dummyTransportFactory, new AddDummyTransportWizard());
		gui.getAddTransportWizards().put(hotseatTransportFactory, new AddHotseatTransportWizard());
		
		gui.getGames().add(new Fortress());
		
		gui.getHostGuis().put(gui.getGames().get(0), new FortressHostGui());
		gui.getJoinGuis().put(gui.getGames().get(0), new FortressJoinGui());
		
		gui.getServerList().add(new ServerInfo("Connexion-server", new JabberTransportFactory(), //
				new JabberConnectionParameters("connexion", "connexion", "binaryfreedom.info", "connexion", 5222), // 
				"connexion-server@binaryfreedom.info"));
		
		gui.start();
	}
}
