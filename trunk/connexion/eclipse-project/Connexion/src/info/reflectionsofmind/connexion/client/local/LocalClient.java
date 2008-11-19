package info.reflectionsofmind.connexion.client.local;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.ui.ClientUI;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.IServer;

import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

		this.waitingFrame = new WaitingFrame();
		this.waitingFrame.setVisible(true);
		this.server.register(this);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void onEnd()
	{
		if (this.clientUI == null) throw new RuntimeException("Server sent end, but start notification has not been received!");
	}

	@Override
	public void onStart(final Player player)
	{
		this.waitingFrame.dispose();
		this.clientUI = new ClientUI(this.server, player);
	}

	@Override
	public void onTurn(final Turn turn)
	{
		if (this.clientUI == null) throw new RuntimeException("Server sent turn, but start notification has not been received!");
	}
	
	@Override
	public void onDisconnect()
	{
		JOptionPane.showMessageDialog(null, "Disconnected on server request.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private final class WaitingFrame extends JFrame
	{
		private static final long serialVersionUID = 1L;

		public WaitingFrame()
		{
			super("Connexion Client");

			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			setLayout(new MigLayout("", "[240, center]", "[80]"));

			add(new Label("Waiting for the game to start..."), "span");
			pack();
		}
	}
}
