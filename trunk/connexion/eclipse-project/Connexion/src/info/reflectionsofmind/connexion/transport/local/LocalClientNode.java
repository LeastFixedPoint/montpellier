package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.IClientNode;
import info.reflectionsofmind.connexion.transport.TransportException;

public class LocalClientNode implements IClientNode
{
	private final ClientLocalTransport clientTransport;
	private final ServerLocalTransport serverTransport;

	public LocalClientNode(ServerLocalTransport serverTransport, ClientLocalTransport clientTransport)
	{
		this.serverTransport = serverTransport;
		this.clientTransport = clientTransport;
	}

	@Override
	public ServerLocalTransport getTransport()
	{
		return this.serverTransport;
	}
	
	@Override
	public void send(String contents) throws TransportException
	{
		this.clientTransport.receive(contents);
	}
}