package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.local.server.DefaultGuiServer;
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

public class ServerUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final DefaultGuiServer server;

	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

	private final JButton startButton;
	private final ConfigPanel configPanel;
	private final JButton addClientButton;

	public ServerUI()
	{
		super("Connexion :: Server");

		this.server = new DefaultGuiServer(this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[]", "[]6[18][18][18]12[18]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "span");

		add(new JLabel("Clients:"), "span");

		addClientPanel();

		this.addClientButton = new JButton(new AbstractAction("Add another client")
		{
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				addClientPanel();
			}
		});

		add(this.addClientButton, "w 148, wrap");

		this.startButton = new JButton(new StartAction());
		add(this.startButton, "w 120, right");

		pack();
		setLocationRelativeTo(null);
	}

	public void addClientPanel()
	{
		final ClientPanel clientPanel = new ClientPanel(this);
		this.panels.add(clientPanel);

		add(clientPanel, "grow, flowy, cell 0 2, wrap");
		pack();
	}

	public void removeClientPanel(final ClientPanel panel)
	{
		if (this.panels.size() > 1)
		{
			remove(panel);
			this.panels.remove(panel);
			pack();
		}
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

	public DefaultGuiServer getServer()
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

			for (final ClientPanel panel : ServerUI.this.panels)
			{
				atLeastOneClient = atLeastOneClient || panel.getState() == State.CONNECTED;
			}

			if (!atLeastOneClient)
			{
				JOptionPane.showMessageDialog(ServerUI.this, "You must have at least one client connected!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ServerUI.this.addClientButton.setEnabled(false);
			ServerUI.this.configPanel.fade();

			for (final ClientPanel panel : ServerUI.this.panels)
			{
				panel.fade();
			}

			setEnabled(false);

			ServerUI.this.server.startGame(ServerUI.this.configPanel.getGameName());
		}
	}
}
