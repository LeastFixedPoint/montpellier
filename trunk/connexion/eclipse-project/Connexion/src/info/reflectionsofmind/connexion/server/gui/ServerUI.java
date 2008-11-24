package info.reflectionsofmind.connexion.server.gui;

import info.reflectionsofmind.connexion.server.local.DefaultGuiServer;
import info.reflectionsofmind.connexion.server.remote.ClientType;

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
	private final static int MAX_CLIENTS = 5;
	private final DefaultGuiServer server;

	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

	private final JButton startButton;
	private final ConfigPanel configPanel;

	public ServerUI()
	{
		super("Connexion :: Server");

		this.server = new DefaultGuiServer(this);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[]", "[]6[18][18][18][18][18][18]12[18]"));

		this.configPanel = new ConfigPanel(this);
		add(this.configPanel, "span");

		add(new JLabel("Clients:"), "span");

		for (int i = 0; i < MAX_CLIENTS; i++)
		{
			final ClientPanel clientPanel = new ClientPanel(this, i);
			this.panels.add(clientPanel);

			add(clientPanel, "span");
		}

		this.startButton = new JButton(new StartAction());

		add(this.startButton, "span, al 50%, w 180");

		pack();
	}

	public void updateInterface()
	{

	}
	
	public synchronized void onClientConnected(ClientPanel panel)
	{
		if (allConnected())
		{
			this.server.startGame(this.configPanel.getGameName());
		}
	}

	private boolean allConnected()
	{
		for (final ClientPanel panel : ServerUI.this.panels)
		{
			if (panel.getClientType() != ClientType.NONE && panel.getClient() == null)
			{
				return false;
			}
		}
		
		return true;
	}

	public DefaultGuiServer getServer()
	{
		return this.server;
	}

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
				atLeastOneClient |= (panel.getClientType() != ClientType.NONE);
			}
			
			if (!atLeastOneClient)
			{
				JOptionPane.showMessageDialog(ServerUI.this, "You must have at least one client!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (final ClientPanel panel : ServerUI.this.panels)
			{
				panel.startListening();
			}

			configPanel.disable();
			
			startButton.setAction(new CancelAction());
		}
	}

	private class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super("Cancel");
		}

		@Override
		public void actionPerformed(ActionEvent event)
		{
			for (final ClientPanel panel : ServerUI.this.panels)
			{
				panel.cancelListening();
			}
			
			startButton.setAction(new StartAction());
		}
	}
}
