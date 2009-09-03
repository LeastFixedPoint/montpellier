package info.reflectionsofmind.connexion.game.fortress.gui.join;

import info.reflectionsofmind.connexion.platform.gui.join.AbstractJoinGamePanel;
import info.reflectionsofmind.connexion.platform.gui.join.IJoinGamePanelFactory;
import info.reflectionsofmind.connexion.platform.gui.join.JoinGameFrame;

public class FortressJoinGamePanelFactory implements IJoinGamePanelFactory
{
	@Override
	public AbstractJoinGamePanel createGamePanel(final JoinGameFrame frame)
	{
		return new FortressJoinGamePanel(frame);
	}
}
