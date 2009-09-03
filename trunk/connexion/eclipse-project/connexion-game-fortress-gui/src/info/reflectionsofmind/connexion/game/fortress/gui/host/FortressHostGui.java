package info.reflectionsofmind.connexion.game.fortress.gui.host;

import info.reflectionsofmind.connexion.game.fortress.server.FortressServerGameFactory;
import info.reflectionsofmind.connexion.platform.game.server.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.gui.host.IHostGameGui;
import info.reflectionsofmind.connexion.platform.gui.host.IHostGamePanelFactory;

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
