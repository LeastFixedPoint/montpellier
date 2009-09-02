package info.reflectionsofmind.connexion.game.fortress.gui.join;

import info.reflecitonsofmind.connexion.platform.gui.join.AbstractJoinGamePanel;
import info.reflecitonsofmind.connexion.platform.gui.join.IJoinGamePanelFactory;
import info.reflecitonsofmind.connexion.platform.gui.join.JoinGameFrame;

public class FortressJoinGamePanelFactory implements IJoinGamePanelFactory
{
	@Override
	public AbstractJoinGamePanel createGamePanel(final JoinGameFrame frame)
	{
		return new FortressJoinGamePanel(frame);
	}
}
