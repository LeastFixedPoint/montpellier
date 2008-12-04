package info.reflectionsofmind.connexion.transport.hotseat;

import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServer;
import info.reflectionsofmind.connexion.transport.TransportException;

public class ServerHotSeatTransport extends AbstractHotSeatTransport
{
	private final ServerNode node;

	public ServerHotSeatTransport(IServer server)
	{
		this.node = new ServerNode(this, server);
	}

	@Override
	protected void doStart() throws TransportException
	{
		final IServer server = this.node.getServer();

		final DefaultLocalClient client = new DefaultLocalClient(server.getSettings());
		final ClientHotSeatTransport clientTransport = new ClientHotSeatTransport(client);
		final IRemoteServer remoteServer = new RemoteServer<AbstractHotSeatTransport, AbstractHotSeatNode>(clientTransport, this.node);

		client.connect(remoteServer);
	}

	@Override
	public void send(AbstractHotSeatNode to, String message) throws TransportException
	{
		to.getTransport().receive(this.node, message);
	}

	public class ServerNode implements AbstractHotSeatNode
	{
		private final ServerHotSeatTransport transport;
		private final IServer server;

		public ServerNode(final ServerHotSeatTransport transport, final IServer server)
		{
			this.transport = transport;
			this.server = server;
		}

		public ServerHotSeatTransport getTransport()
		{
			return this.transport;
		}
		
		public IServer getServer()
		{
			return this.server;
		}
	}
}
