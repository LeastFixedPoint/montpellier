package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflectionsofmind.connexion.platform.game.server.IServerGameFactory;

public interface IHostGameGui
{
	IHostGamePanelFactory getServerGamePanelFactory();
	
	IServerGameFactory getGameFactory();
}
