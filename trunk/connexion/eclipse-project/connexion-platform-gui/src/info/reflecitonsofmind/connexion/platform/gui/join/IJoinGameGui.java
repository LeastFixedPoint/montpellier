package info.reflecitonsofmind.connexion.platform.gui.join;

import info.reflectionsofmind.connexion.platform.game.client.IClientGameFactory;

public interface IJoinGameGui
{
	IJoinGamePanelFactory getGamePanelFactory();
	
	IClientGameFactory getGameFactory();
}
