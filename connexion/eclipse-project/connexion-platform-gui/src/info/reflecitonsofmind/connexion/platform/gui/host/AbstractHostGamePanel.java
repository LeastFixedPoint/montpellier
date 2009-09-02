package info.reflecitonsofmind.connexion.platform.gui.host;

import javax.swing.JPanel;

public abstract class AbstractHostGamePanel extends JPanel
{
	private final HostGameFrame hostGameFrame;
	
	public AbstractHostGamePanel(final HostGameFrame frame)
	{
		this.hostGameFrame = frame;
	}
	
	public final HostGameFrame getHostGameFrame()
	{
		return this.hostGameFrame;
	}
}
