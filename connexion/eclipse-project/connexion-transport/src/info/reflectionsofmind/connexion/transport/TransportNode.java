package info.reflectionsofmind.connexion.transport;

public class TransportNode
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
}
