package info.reflectionsofmind.connexion;


import info.reflectionsofmind.connexion.ui.LocalGuiClient;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Application
{
	public static void main(final String[] args)
	{
		new Application().start();
	}

	private void start()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

			final IServer server = new LocalServer();
			
			new LocalGuiClient(server);
			
			server.startGame();
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
}
