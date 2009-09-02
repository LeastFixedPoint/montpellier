package info.reflecitonsofmind.connexion.platform.gui.join;

import javax.swing.JPanel;

public abstract class AbstractJoinGamePanel extends JPanel
{
	private final JoinGameFrame joinGameFrame;
	
	public AbstractJoinGamePanel(final JoinGameFrame frame)
	{
		this.joinGameFrame = frame;
	}
	
	public final JoinGameFrame getJoinGameFrame()
	{
		return this.joinGameFrame;
	}
}
