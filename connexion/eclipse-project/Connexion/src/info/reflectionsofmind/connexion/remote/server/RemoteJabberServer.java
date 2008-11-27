package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class RemoteJabberServer extends AbstractRemoteServer implements MessageListener
{
	private final JabberAddress jabberClient;
	private final JabberAddress jabberServer;
	private XMPPConnection connection;
	private Chat chat;

	public RemoteJabberServer(final JabberAddress jabberClient, final JabberAddress jabberServer)
	{
		this.jabberClient = jabberClient;
		this.jabberServer = jabberServer;
	}

	@Override
	public void connect(final IClient client) throws ServerConnectionException, RemoteServerException
	{
		final ClientToServer_ClientConnectionRequestEvent event = new ClientToServer_ClientConnectionRequestEvent(client.getName());
		final String string = event.encode().getString();

		final ConnectionConfiguration clientConfig = new ConnectionConfiguration(this.jabberClient.getHost());
		clientConfig.setSASLAuthenticationEnabled(false);

		try
		{
			this.connection = new XMPPConnection(clientConfig);
			this.connection.connect();
			this.connection.login(this.jabberClient.getLogin(), this.jabberClient.getPassword());

			final String address = this.jabberServer.getLogin() + "@" + this.jabberServer.getHost();
			this.chat = this.connection.getChatManager().createChat(address, this);

			this.chat.sendMessage(string);
		}
		catch (final XMPPException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}
	
	public void disconnect()
	{
		this.connection.disconnect();
	}

	@Override
	public void sendTurn(final Turn turn) throws ServerConnectionException, RemoteServerException
	{
		send(new ClientToServer_TurnEvent(turn));
	}

	@Override
	public void sendMessage(final String message) throws ServerConnectionException, RemoteServerException
	{
		send(new ClientToServer_MessageEvent(message));
	}
	
	private void send(ClientToServerEvent<?> event) throws ServerConnectionException
	{ 
		try
		{
			this.chat.sendMessage(event.encode().getString());
		}
		catch (final XMPPException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}

	// ============================================================================================
	// === JABBER MESSAGE LISTENER IMPLEMENTATION
	// ============================================================================================

	@Override
	public void processMessage(final Chat chat, final Message message)
	{
		System.out.println(message.getBody());
		this.connection.disconnect();
	}
}
