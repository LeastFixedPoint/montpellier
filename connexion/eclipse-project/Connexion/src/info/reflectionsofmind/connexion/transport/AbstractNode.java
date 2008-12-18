package info.reflectionsofmind.connexion.transport;

public abstract class AbstractNode implements INode
{
	@Override
	public final void send(String message) throws TransportException
	{
		getTransport().send(this, message);
	}
}
