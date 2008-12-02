package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.local.client.gui.join.JoinGameWindow;
import info.reflectionsofmind.connexion.local.server.gui.HostGameWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame
{
	public MainWindow()
	{
		super("Connexion");

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[240, center]", "[24]6[24]6[24]6[24]6[24]24[24]"));

		add(new JLabel("Connexion"), "span");

		add(new JButton(new HostGameAction("Host game")), "span, grow");

		add(new JButton(new JoinGameAction("Join game")), "span, grow");

		add(new JButton("Play online"), "span, grow");

		add(new JButton("Configure"), "span, grow");

		add(new JButton(new ExitAction("Exit")), "span");

		pack();
		setVisible(true);
	}

	private final class ExitAction extends AbstractAction
	{
		private ExitAction(String name)
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
		private JoinGameAction(String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			new JoinGameWindow().setVisible(true);
		}
	}

	private final class HostGameAction extends AbstractAction
	{
		private HostGameAction(String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			new HostGameWindow().setVisible(true);
		}
	}
}
