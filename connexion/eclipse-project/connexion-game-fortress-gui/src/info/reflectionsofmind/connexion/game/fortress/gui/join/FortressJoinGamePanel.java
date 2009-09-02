package info.reflectionsofmind.connexion.game.fortress.gui.join;

import info.reflecitonsofmind.connexion.platform.gui.join.AbstractJoinGamePanel;
import info.reflecitonsofmind.connexion.platform.gui.join.JoinGameFrame;

import javax.swing.BorderFactory;

public class FortressJoinGamePanel extends AbstractJoinGamePanel
{
	public FortressJoinGamePanel(final JoinGameFrame frame)
	{
		super(frame);
		
		setBorder(BorderFactory.createTitledBorder("Fortress"));
	}
}
