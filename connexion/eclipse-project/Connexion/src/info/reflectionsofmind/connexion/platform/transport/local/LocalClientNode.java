package info.reflectionsofmind.connexion.platform.transport.local;

import info.reflectionsofmind.connexion.platform.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.transport.TransportException;

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
	
	public LocalClientTransport getClientTransport()
	{
		return this.clientTransport;
	}
	
	@Override
	public void send(String contents) throws TransportException
	{
		this.clientTransport.receive(contents);
	}
}