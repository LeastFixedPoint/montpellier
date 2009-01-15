package info.reflectionsofmind.connexion.gui;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.gui.host.HostGameFrame;
import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.gui.settings.SettingsDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JConnexionFrame
{
	public MainFrame(IApplication application)
	{
		super(application);
		setTitle("Connexion");

		setLocationRelativeTo(null);
		setLayout(new MigLayout("", "[240, center]", "[24]6[24]6[24]6[24]6[24]6[24]24[24]"));

		add(new JLabel("Connexion"), "span");
		add(new JButton(new HostGameAction("Host game")), "span, grow");
		add(new JButton(new JoinGameAction("Join game")), "span, grow");
		add(new JButton("Play online"), "span, grow");
		add(new JButton("Create hub"), "span, grow");
		add(new JButton(new SettingsAction("Settings")), "span, grow");
		add(new JButton(new ExitAction("Exit")), "span");

		pack();
		setVisible(true);
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
			new JoinGameFrame(getApplication()).setVisible(true);
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
			new HostGameFrame(getApplication()).setVisible(true);
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
			new SettingsDialog(MainFrame.this).setVisible(true);
		}
	}
}
