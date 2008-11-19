package info.reflectionsofmind.connexion.client.local;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.ui.ClientUI;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.DisconnectReason;
import info.reflectionsofmind.connexion.server.IServer;

import java.awt.Label;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class LocalClient implements IClient
{
	private final IServer server;
	private final String name;
	private final WaitingFrame waitingFrame;
	private ClientUI clientUI;

	public LocalClient(final IServer server, final String name)
	{
		this.server = server;
		this.name = name;

		this.waitingFrame = new WaitingFrame(name);
		this.waitingFrame.setVisible(true);
		this.server.register(this);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void onStart(final Player player)
	{
		this.clientUI = new ClientUI(this.server, this, player);
		this.waitingFrame.dispose();
	}

	@Override
	public void onTurn(final Turn turn)
	{
		this.clientUI.onTurn(turn);
	}

	@Override
	public void onDisconnect(IClient client, DisconnectReason reason)
	{
		if (this.clientUI != null)
		{
			this.clientUI.onDisconnect(client, reason);
		}
		else if (client == this)
		{
			this.waitingFrame.dispose();
		}
	}

	private final class WaitingFrame extends JFrame
	{
		private static final long serialVersionUID = 1L;

		public WaitingFrame(final String playerName)
		{
			super("Connexion " + playerName);

			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			setLayout(new MigLayout("", "[240, center]", "[80]"));

			add(new Label("Waiting for the game to start..."), "span");
			pack();
		}
		
		@Override
		public void dispose()
		{
			if (server.getGame() == null)
			{
				server.disconnect(LocalClient.this, DisconnectReason.CLIENT_REQUEST);
			}
			
			super.dispose();
		}
	}
}
