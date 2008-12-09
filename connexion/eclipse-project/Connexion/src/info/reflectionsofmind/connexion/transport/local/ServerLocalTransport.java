package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.server.LocalRemoteServer;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class ServerLocalTransport extends AbstractLocalTransport
{
	private final ServerLocalNode node;
	private final List<LocalRemoteServer> remoteServers = new ArrayList<LocalRemoteServer>();

	public ServerLocalTransport(final IServer server)
	{
		this.node = new ServerLocalNode(this, server);
	}

	@Override
	protected void doStart() throws TransportException
	{
		this.remoteServers.add(new LocalRemoteServer(new ClientLocalTransport(), this.node));
	}
	
	@Override
	public void send(INode to, String message) throws TransportException
	{
		((AbstractLocalNode)to).getTransport().receive(this.node, message);
	}

	public List<LocalRemoteServer> getRemoteServers()
	{
		return ImmutableList.copyOf(this.remoteServers);
	}

	public class ServerLocalNode implements AbstractLocalNode
	{
		private final ServerLocalTransport transport;
		private final IServer server;

		public ServerLocalNode(final ServerLocalTransport transport, final IServer server)
		{
			this.transport = transport;
			this.server = server;
		}

		public ServerLocalTransport getTransport()
		{
			return this.transport;
		}

		public IServer getServer()
		{
			return this.server;
		}
	}
}
