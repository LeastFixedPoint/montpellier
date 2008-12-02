package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.local.server.DefaultLocalServer;
import info.reflectionsofmind.connexion.local.server.gui.ClientPanel.State;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

public class HostGameWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final DefaultLocalServer server;

	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final JButton addClientButton;

	public HostGameWindow()
	{
		super("Connexion :: Server");

		this.server = new DefaultLocalServer(this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[]", "[]6[18][18][18]12[18]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "span");

		add(new JLabel("Clients:"), "span");

		add(new ClientsPanel(this));

		this.startButton = new JButton(new StartAction());
		add(this.startButton, "w 120, right");

		pack();
		setLocationRelativeTo(null);
	}

	public void updateInterface()
	{

	}

	public void onClientConnected(final ClientPanel panel)
	{
		this.server.add(panel.getClient());
	}

	public void onClientDisconnected(final ClientPanel panel)
	{
		this.server.remove(panel.getClient());
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
			boolean atLeastOneClient = false;

			for (final ClientPanel panel : HostGameWindow.this.panels)
			{
				atLeastOneClient = atLeastOneClient || panel.getState() == State.CONNECTED;
			}

			if (!atLeastOneClient)
			{
				JOptionPane.showMessageDialog(HostGameWindow.this, "You must have at least one client connected!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			HostGameWindow.this.addClientButton.setEnabled(false);
			HostGameWindow.this.configPanel.fade();

			for (final ClientPanel panel : HostGameWindow.this.panels)
			{
				panel.fade();
			}

			setEnabled(false);

			HostGameWindow.this.server.startGame(HostGameWindow.this.configPanel.getGameName());
		}
	}
}
