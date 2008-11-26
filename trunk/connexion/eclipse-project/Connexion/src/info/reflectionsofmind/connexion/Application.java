package info.reflectionsofmind.connexion;

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

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
//					new ServerUI().setVisible(true);
					new MainMenuFrame().setVisible(true);
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
