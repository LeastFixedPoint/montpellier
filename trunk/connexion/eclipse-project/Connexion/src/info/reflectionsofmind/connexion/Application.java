package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.server.gui.ServerUI;

import java.lang.Thread.UncaughtExceptionHandler;

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
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
			{
				@Override
				public void uncaughtException(Thread t, Throwable e)
				{
					e.printStackTrace();
				}
			});

			new ServerUI().setVisible(true);
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
}
