package info.reflectionsofmind.connexion.game.fortress.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.host.AbstractHostGamePanel;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;

import javax.swing.BorderFactory;

public class FortressHostGamePanel extends AbstractHostGamePanel
{
	public FortressHostGamePanel(final HostGameFrame frame)
	{
		super(frame);
		
		setBorder(BorderFactory.createTitledBorder("Fortress"));
	}
}
