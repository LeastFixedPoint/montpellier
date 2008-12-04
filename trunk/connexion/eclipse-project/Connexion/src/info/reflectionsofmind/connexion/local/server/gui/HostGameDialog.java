package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.MainWindow;
import info.reflectionsofmind.connexion.local.server.DefaultLocalServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final DefaultLocalServer server;

	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final ClientsPanel clientsPanel;

	public HostGameDialog(final MainWindow parent)
	{
		super(parent, "Connexion :: Host game", true);

		this.server = new DefaultLocalServer(parent.getApplication().getSettings());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(true);
		setLayout(new MigLayout("", "[]", "[]6[18][18][18]12[18]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "span");

		this.clientsPanel = new ClientsPanel(this);
		add(this.clientsPanel);

		this.startButton = new JButton(new StartAction());
		add(this.startButton, "w 120, right");

		pack();
		setLocationRelativeTo(null);
	}

	// ====================================================================================================
	// === GETTERS AND SETTERS
	// ====================================================================================================

	public DefaultLocalServer getServer()
	{
		return this.server;
	}

	// ====================================================================================================
	// === ACTIONS
	// ====================================================================================================

	private class StartAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public StartAction()
		{
			super("Start game");
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			if (ServerUtil.getPlayers(getServer()).isEmpty())
			{
				JOptionPane.showMessageDialog(HostGameDialog.this, "You must have at least one player!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			HostGameDialog.this.configPanel.setEnabled(false);
			HostGameDialog.this.clientsPanel.setEnabled(false);

			setEnabled(false);

			HostGameDialog.this.server.startGame(HostGameDialog.this.configPanel.getGameName());
		}
	}
}
