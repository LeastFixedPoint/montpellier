package info.reflecitonsofmind.connexion.platform.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
	private final ConnexionGUI gui;

	public MainFrame(final ConnexionGUI gui)
	{
		this.gui = gui;
	}
	
	public ConnexionGUI getGui()
	{
		return this.gui;
	}
}
