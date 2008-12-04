package info.reflectionsofmind.connexion.transport.hotseat;

import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.transport.TransportException;

public class ClientHotSeatTransport extends AbstractHotSeatTransport
{
	private final ClientNode node;

	public ClientHotSeatTransport(IClient client)
	{
		this.node = new ClientNode(this, client);
	}
	
	@Override
	public void send(AbstractHotSeatNode to, String message) throws TransportException
	{
		to.getTransport().receive(this.node, message);
	}

	public class ClientNode implements AbstractHotSeatNode
	{
		private final ClientHotSeatTransport transport;
		private final IClient client;

		public ClientNode(final ClientHotSeatTransport transport, IClient client)
		{
			this.transport = transport;
			this.client = client;
		}

		public ClientHotSeatTransport getTransport()
		{
			return this.transport;
		}
		
		public IClient getClient()
		{
			return this.client;
		}
	}
}
