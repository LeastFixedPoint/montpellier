package info.reflectionsofmind.connexion.game.fortress.gui.join;

import info.reflectionsofmind.connexion.game.fortress.client.FortressClientGameFactory;
import info.reflectionsofmind.connexion.platform.game.client.IClientGameFactory;
import info.reflectionsofmind.connexion.platform.gui.join.IJoinGameGui;
import info.reflectionsofmind.connexion.platform.gui.join.IJoinGamePanelFactory;

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
