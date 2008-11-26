package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.local.client.IClient;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class RemoteJabberServer extends AbstractRemoteServer
{
	private final ConnectionConfiguration config;
	private final String serverAddress;
	private XMPPConnection connection;

	public RemoteJabberServer(final ConnectionConfiguration config, final String serverAddress)
	{
		this.config = config;
		this.serverAddress = serverAddress;
	}

	@Override
	public void connect(final IClient client) throws ServerConnectionException, RemoteServerException
	{
		final ClientToServer_ClientConnectionRequestEvent event = new ClientToServer_ClientConnectionRequestEvent(client.getName());
		final String string = event.encode().getString();

		this.connection = new XMPPConnection(this.config);

		try
		{
			this.connection.connect();
			this.connection.login("connexion", "connexion");
		}
		catch (XMPPException exception)
		{
			throw new RuntimeException(exception);
		}

		final Message message = new Message(serverAddress, Message.Type.normal);
		message.setBody("body");
		message.setSubject("subj");
		
		this.connection.sendPacket(message);
		this.connection.disconnect();
	}

	@Override
	public void sendTurn(final Turn turn) throws ServerConnectionException, RemoteServerException
	{
	}
}
