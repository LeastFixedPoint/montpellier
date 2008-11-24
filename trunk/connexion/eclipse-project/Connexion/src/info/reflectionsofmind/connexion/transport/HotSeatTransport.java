package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.client.remote.AbstractRemoteServer;
import info.reflectionsofmind.connexion.client.remote.IRemoteServer;
import info.reflectionsofmind.connexion.client.remote.RemoteServerException;
import info.reflectionsofmind.connexion.client.remote.ServerConnectionException;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.server.local.IServer;
import info.reflectionsofmind.connexion.server.remote.AbstractRemoteClient;
import info.reflectionsofmind.connexion.server.remote.ClientConnectionException;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

public class HotSeatTransport
{
	private IRemoteClient remoteClient;
	private IRemoteServer remoteServer;

	public void setServer(IServer server)
	{
		this.remoteServer = new RemoteServer(server);
	}

	public void setClient(IClient client)
	{
		this.remoteClient = new RemoteClient(client);
	}

	public IRemoteClient getRemoteClient()
	{
		if (this.remoteClient == null) throw new RuntimeException("Client is not set yet.");

		return this.remoteClient;
	}

	public IRemoteServer getRemoteServer()
	{
		if (this.remoteServer == null) throw new RuntimeException("Server is not set yet.");

		return this.remoteServer;
	}

	private class RemoteClient extends AbstractRemoteClient
	{
		private final IClient client;

		public RemoteClient(IClient client)
		{
			this.client = client;
		}
		
		@Override
		public void sendStart(StartEvent event) throws ClientConnectionException
		{
			client.onStart(event);
		}

		@Override
		public void sendTurn(ServerTurnEvent event) throws ClientConnectionException
		{
			client.onTurn(event);
		}

		@Override
		public void disconnect(DisconnectReason reason) throws ClientConnectionException
		{
			throw new UnsupportedOperationException("Disconnects not supported yet");
		}

		@Override
		public String getName()
		{
			return client.getName();
		}
	}

	private class RemoteServer extends AbstractRemoteServer
	{
		private final IServer server;

		public RemoteServer(IServer server)
		{
			this.server = server;
		}

		@Override
		public void connect(IClient client) throws ServerConnectionException, RemoteServerException
		{
			this.server.onConnectionRequest(new ConnectionEvent(remoteClient));
		}

		@Override
		public void sendTurn(final Turn turn) throws RemoteServerException
		{
			this.server.onTurn(new ClientTurnEvent(HotSeatTransport.this.remoteClient, turn));
		}
	}
}
