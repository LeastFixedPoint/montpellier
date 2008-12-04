package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.transport.ITransport;

public class ClientType
{
	private final ITransport<?> transport;

	public ClientType(final ITransport<?> transport)
	{
		this.transport = transport;
	}

	public ITransport<?> getTransport()
	{
		return this.transport;
	}
	
	@Override
	public String toString()
	{
		return transport == null ? "None" : this.transport.getName();
	}
}
