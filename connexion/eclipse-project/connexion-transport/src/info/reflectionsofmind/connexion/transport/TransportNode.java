package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;

public class TransportNode implements INamed
{
	private final ITransport transport;
	private final String address;
	
	public TransportNode(final ITransport transport, final String address)
	{
		this.transport = transport;
		this.address = address;
	}
	
	public ITransport getTransport()
	{
		return this.transport;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public void send(final String contents)
	{
		getTransport().send(this, contents);
	}
	
	public String getName()
	{
		return getAddress();
	}
}
