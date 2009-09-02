package info.reflecitonsofmind.connexion.platform.gui;

import info.reflecitonsofmind.connexion.platform.gui.connect.ConnectFrame;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame
{
	private final ConnexionGUI gui;
	
	public MainFrame(final ConnexionGUI gui)
	{
		super("Connexion");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[240::, fill]", "[36, fill][36, fill]24[36, fill]"));
		
		add(new JButton(new HostAction()), "wrap");
		add(new JButton(new JoinAction()), "wrap");
		add(new JButton(new ExitAction()), "wrap");
		
		this.gui = gui;
		
		pack();
		setLocationRelativeTo(null);
	}
	
	public ConnexionGUI getGui()
	{
		return this.gui;
	}

	private final class HostAction extends AbstractAction
	{
		public HostAction()
		{
			super("Host a new game");
		}
		
		public void actionPerformed(final ActionEvent arg0)
		{
			setVisible(false);
			new HostGameFrame(MainFrame.this, getGui().getHostGameGuis().get(0)).setVisible(true);
		}
	}

	private final class JoinAction extends AbstractAction
	{
		public JoinAction()
		{
			super("Connect to a game");
		}
		
		public void actionPerformed(final ActionEvent e)
		{
			setVisible(false);
			new ConnectFrame(MainFrame.this, getGui().getJoinGameGuis().get(0)).setVisible(true);
		}
	}

	private final class ExitAction extends AbstractAction
	{
		public ExitAction()
		{
			super("Exit");
		}
		
		public void actionPerformed(final ActionEvent e)
		{
			dispose();
		}
	}
}
