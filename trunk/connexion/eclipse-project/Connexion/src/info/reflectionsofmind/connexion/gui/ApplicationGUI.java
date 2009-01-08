package info.reflectionsofmind.connexion.gui;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.config.ConfigurationUtil;
import info.reflectionsofmind.connexion.util.Component;
import info.reflectionsofmind.connexion.util.Configuration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class ApplicationGUI extends Component implements IApplication
{
	public static void main(final String[] args)
	{
		Configuration configuration = ConfigurationUtil.loadFromFile("connexion.properties");
		final ApplicationGUI application = new ApplicationGUI();
		application.configure(configuration);
		application.start();
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
					final MainFrame mainFrame = createChild("gui", MainFrame.class);
					mainFrame.setVisible(true);
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
