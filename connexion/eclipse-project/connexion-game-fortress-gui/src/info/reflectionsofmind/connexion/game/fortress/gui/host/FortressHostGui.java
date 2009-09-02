package info.reflectionsofmind.connexion.game.fortress.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.host.IHostGameGui;
import info.reflecitonsofmind.connexion.platform.gui.host.IHostGamePanelFactory;
import info.reflectionsofmind.connexion.game.fortress.server.FortressServerGameFactory;
import info.reflectionsofmind.connexion.platform.game.server.IServerGameFactory;

public class FortressHostGui implements IHostGameGui
{
	private final IServerGameFactory gameFactory = new FortressServerGameFactory();
	private final IHostGamePanelFactory gamePanelFactory = new FortressHostGamePanelFactory();
	
	public IServerGameFactory getGameFactory()
	{
		return this.gameFactory;
	}
	
	public IHostGamePanelFactory getServerGamePanelFactory()
	{
		return this.gamePanelFactory;
	}
}
