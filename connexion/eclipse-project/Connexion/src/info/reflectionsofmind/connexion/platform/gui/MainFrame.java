package info.reflectionsofmind.connexion.platform.gui;

import info.reflectionsofmind.connexion.DefaultApplication;
import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflectionsofmind.connexion.platform.gui.join.JoinGameFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class MainFrame extends JConnexionFrame
{
	public MainFrame(final IApplication application)
	{
		super(application);
		setTitle("Connexion");

		setLocationRelativeTo(null);
		setLayout(new MigLayout("", "[240, center]", "[24]6[24]6[24]6[24]6[24]24[24]"));

		add(new JLabel("Connexion"), "span");
		add(new JButton(new HostGameAction("Host game")), "span, grow");
		add(new JButton(new JoinGameAction("Join game")), "span, grow");
		add(new JButton("Play online"), "span, grow");
		add(new JButton("Create hub"), "span, grow");
		add(new JButton(new ExitAction("Exit")), "span");

		pack();
		setVisible(true);
	}

	public static void main(final String[] args) throws Exception
	{
		try
		{
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					new MainFrame(new DefaultApplication()).setVisible(true);
				}
			});
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}

	private final class ExitAction extends AbstractAction
	{
		private ExitAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
			dispose();
		}
	}

	private final class JoinGameAction extends AbstractAction
	{
		private JoinGameAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			new JoinGameFrame(getApplication().newClient()).setVisible(true);
		}
	}

	private final class HostGameAction extends AbstractAction
	{
		private HostGameAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			new HostGameFrame(getApplication().newServer()).setVisible(true);
		}
	}
}
