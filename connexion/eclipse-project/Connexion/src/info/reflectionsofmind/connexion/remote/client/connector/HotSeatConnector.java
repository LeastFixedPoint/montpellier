package info.reflectionsofmind.connexion.remote.client.connector;

import info.reflectionsofmind.connexion.hotseat.HotSeatTransport;
import info.reflectionsofmind.connexion.local.client.DefaultGuiClient;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;

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
	public void startListening()
	{
		index++;

		this.transport = new HotSeatTransport();
		this.transport.setServer(this.server);

		final DefaultGuiClient client = new DefaultGuiClient(this.transport.getRemoteServer(), "Client #" + index);
		this.transport.setClient(client);

		fireConnected(this.transport.getRemoteClient());
	}

	@Override
	public void stopListening()
	{
		try
		{
			this.transport.getRemoteClient().disconnect(ServerSideDisconnectReason.SERVER_REQUEST);
		}
		catch (final ClientConnectionException exception)
		{
			exception.printStackTrace();
		}
	}
}
