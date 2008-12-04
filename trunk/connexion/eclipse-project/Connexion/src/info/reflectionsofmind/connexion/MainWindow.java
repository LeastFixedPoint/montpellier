package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.local.client.gui.join.JoinGameDialog;
import info.reflectionsofmind.connexion.local.server.gui.HostGameDialog;
import info.reflectionsofmind.connexion.local.settings.SettingsDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame
{
	private final Application application;

	public MainWindow(Application application)
	{
		super("Connexion");

		this.application = application;

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[240, center]", "[24]6[24]6[24]6[24]6[24]24[24]"));

		add(new JLabel("Connexion"), "span");

		add(new JButton(new HostGameAction("Host game")), "span, grow");

		add(new JButton(new JoinGameAction("Join game")), "span, grow");

		add(new JButton("Play online"), "span, grow");

		add(new JButton(new SettingsAction("Settings")), "span, grow");

		add(new JButton(new ExitAction("Exit")), "span");

		pack();
		setVisible(true);
	}

	public Application getApplication()
	{
		return this.application;
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
			new JoinGameDialog(MainWindow.this).setVisible(true);
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
			new HostGameDialog(MainWindow.this).setVisible(true);
		}
	}

	private final class SettingsAction extends AbstractAction
	{
		private SettingsAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			new SettingsDialog(MainWindow.this).setVisible(true);
		}
	}
}
