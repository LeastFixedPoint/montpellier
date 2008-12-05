package info.reflectionsofmind.connexion.gui;

import info.reflectionsofmind.connexion.local.Settings;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class ApplicationGUI
{
	private Settings settings;
	
	public static void main(final String[] args)
	{
		new ApplicationGUI().start();
	}

	private void start()
	{
		this.settings = new Settings();
		this.settings.load();
		
		try
		{
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					new MainWindow(ApplicationGUI.this).setVisible(true);
				}
			});
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}

	public Settings getSettings()
	{
		return this.settings;
	}
}
