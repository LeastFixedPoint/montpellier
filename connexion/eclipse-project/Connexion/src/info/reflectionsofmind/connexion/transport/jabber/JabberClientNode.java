package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.IServerTransport.IClientNode;

public class JabberClientNode implements IClientNode
{
	private final JabberServerTransport transport;

	public JabberClientNode(JabberServerTransport transport)
	{
		this.transport = transport;
	}

	public JabberServerTransport getTransport()
	{
		return this.transport;
	}
	
	@Override
	public void send(String contents) throws TransportException
	{
		getTransport().send(contents);
	}
}
