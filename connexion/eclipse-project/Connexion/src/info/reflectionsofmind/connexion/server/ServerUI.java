package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.client.ClientType;
import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class ServerUI extends JFrame implements IServerListener
{
	private static final long serialVersionUID = 1L;
	private final static int MAX_CLIENTS = 5;
	private final IServer server;

	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

	private final JButton startButton;
	private final ConfigPanel configPanel;

	static final Map<String, ClientType> CLIENT_TYPES = new LinkedHashMap<String, ClientType>()
	{
		private static final long serialVersionUID = 1L;

		{
			put("", ClientType.NONE);
			put("Local", ClientType.LOCAL);
			put("Bot", ClientType.BOT);
			put("Spectator", ClientType.SPECTATOR);
			put("Jabber", ClientType.JABBER);
			put("Online", ClientType.ONLINE);
		}
	};

	public ServerUI()
	{
		super("Connexion Server");

		this.server = new SimpleServer();
		this.server.addServerListener(this);

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
		this.startButton.setEnabled(false);

		add(this.startButton, "span, al 50%, w 180");

		pack();
	}

	@Override
	public void dispose()
	{
		for (final ClientPanel panel : this.panels)
		{
			if (panel.getClient() != null)
			{
				this.server.disconnect(panel.getClient(), DisconnectReason.SERVER_SHUTDOWN);
			}
		}

		super.dispose();
	}

	void updateStartButton()
	{
		for (final ClientPanel panel : this.panels)
		{
			if (panel.getClient() != null)
			{
				this.startButton.setEnabled(true);
				return;
			}
		}

		this.startButton.setEnabled(false);
	}

	public IServer getServer()
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
		public void actionPerformed(final ActionEvent arg0)
		{
			ServerUI.this.server.startGame(configPanel.getGameName());
			setEnabled(false);

			for (final ClientPanel panel : ServerUI.this.panels)
			{
				panel.disable();
			}
			
			configPanel.disable();
		}
	}

	// ============================================================================================
	// === SERVER LISTENER METHODS
	// ============================================================================================

	@Override
	public void onClientRegister(final IClient client)
	{
	}

	@Override
	public void onClientDisconnect(final IClient client, final DisconnectReason reason)
	{
		for (final ClientPanel panel : this.panels)
		{
			if (panel.getClient() == client)
			{
				panel.onDisconnect(reason);
			}
		}
	}

	@Override
	public void onGameEnd()
	{
	}

	@Override
	public void onGameStart()
	{
	}

	@Override
	public void onTurn(final Turn turn)
	{
	}
}
