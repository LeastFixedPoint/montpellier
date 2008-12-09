package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public class ClientLocalTransport extends AbstractLocalTransport
{
	private final ClientLocalNode node;

	public ClientLocalTransport()
	{
		this.node = new ClientLocalNode(this);
	}
	
	@Override
	public void send(INode to, String message) throws TransportException
	{
		((AbstractLocalNode)to).getTransport().receive(this.node, message);
	}

	public class ClientLocalNode implements AbstractLocalNode
	{
		private final ClientLocalTransport transport;

		public ClientLocalNode(final ClientLocalTransport transport)
		{
			this.transport = transport;
		}

		public ClientLocalTransport getTransport()
		{
			return this.transport;
		}
	}
}
