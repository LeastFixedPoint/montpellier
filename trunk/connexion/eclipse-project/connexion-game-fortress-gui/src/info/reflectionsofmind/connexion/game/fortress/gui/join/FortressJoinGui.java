package info.reflectionsofmind.connexion.game.fortress.gui.join;

import info.reflecitonsofmind.connexion.platform.gui.join.IJoinGameGui;
import info.reflecitonsofmind.connexion.platform.gui.join.IJoinGamePanelFactory;
import info.reflectionsofmind.connexion.game.fortress.server.FortressClientGameFactory;
import info.reflectionsofmind.connexion.platform.game.client.IClientGameFactory;

public class FortressJoinGui implements IJoinGameGui
{
	private final IClientGameFactory gameFactory = new FortressClientGameFactory();
	private final IJoinGamePanelFactory gamePanelFactory = new FortressJoinGamePanelFactory();
	
	public IClientGameFactory getGameFactory()
	{
		return this.gameFactory;
	}
	
	public IJoinGamePanelFactory getGamePanelFactory()
	{
		return this.gamePanelFactory;
	}
}
