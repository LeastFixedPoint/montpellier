package info.reflectionsofmind.connexion.game.fortress.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.host.AbstractHostGamePanel;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflecitonsofmind.connexion.platform.gui.host.IHostGamePanelFactory;

public class FortressHostGamePanelFactory implements IHostGamePanelFactory
{
	@Override
	public AbstractHostGamePanel createGamePanel(final HostGameFrame hostGameFrame)
	{
		return new FortressHostGamePanel(hostGameFrame);
	}
}
