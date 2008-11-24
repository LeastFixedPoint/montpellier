package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.server.gui.ServerUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

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
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());

			// UIManager.setLookAndFeel(org.jvnet.substance.SubstanceLookAndFeel.class.getName());

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					new ServerUI().setVisible(true);
//					new MainMenuFrame().setVisible(true);
				}
			});
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
}
