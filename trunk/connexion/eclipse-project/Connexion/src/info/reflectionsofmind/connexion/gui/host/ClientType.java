package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.transport.ITransport;

public class ClientType
{
	public final static ClientType NONE = new ClientType(null);
	private final ITransport transport;

	public ClientType(final ITransport transport)
	{
		this.transport = transport;
	}

	public ITransport getTransport()
	{
		return this.transport;
	}
	
	@Override
	public String toString()
	{
		return transport == null ? "None" : this.transport.getName();
	}
}
