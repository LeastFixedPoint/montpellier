package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.IClientNode;
import info.reflectionsofmind.connexion.transport.TransportException;

public class LocalClientNode implements IClientNode
{
	private final LocalClientTransport clientTransport;
	private final LocalServerTransport serverTransport;

	public LocalClientNode(LocalServerTransport serverTransport, LocalClientTransport clientTransport)
	{
		this.serverTransport = serverTransport;
		this.clientTransport = clientTransport;
	}

	@Override
	public LocalServerTransport getTransport()
	{
		return this.serverTransport;
	}
	
	@Override
	public void send(String contents) throws TransportException
	{
		this.clientTransport.receive(contents);
	}
}