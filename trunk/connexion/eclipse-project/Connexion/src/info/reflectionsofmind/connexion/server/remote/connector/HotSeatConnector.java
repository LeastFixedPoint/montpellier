package info.reflectionsofmind.connexion.server.remote.connector;

import info.reflectionsofmind.connexion.client.local.DefaultGuiClient;
import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.server.local.IServer;
import info.reflectionsofmind.connexion.server.remote.ClientConnectionException;
import info.reflectionsofmind.connexion.transport.HotSeatTransport;

public class HotSeatConnector extends AbstractClientConnector
{
	private static int index = 0;
	private HotSeatTransport transport;
	private final IServer server;

	public HotSeatConnector(final IServer server)
	{
		this.server = server;
	}

	@Override
	public void listen()
	{
		final String name = "Client #" + index;

		index++;

		if (name == null) return; // throw new ConnectionFailedException("Client cancelled.");

		this.transport = new HotSeatTransport();
		this.transport.setServer(this.server);

		final DefaultGuiClient client = new DefaultGuiClient(this.transport.getRemoteServer(), "Client");
		this.transport.setClient(client);

		fireConnected(this.transport.getRemoteClient());
	}

	@Override
	public void disconnect()
	{
		try
		{
			this.transport.getRemoteClient().disconnect(DisconnectReason.SERVER_REQUEST);
		}
		catch (final ClientConnectionException exception)
		{
			exception.printStackTrace();
		}
	}
}
