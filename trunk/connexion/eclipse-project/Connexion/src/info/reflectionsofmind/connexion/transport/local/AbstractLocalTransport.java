package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

public abstract class AbstractLocalTransport extends AbstractTransport implements INode
{
	@Override
	public final void stop() throws TransportException
	{
		// Nothing to do
	}
	
	@Override
	public final String getName()
	{
		return "Local";
	}
	
	@Override
	public void send(INode to, String message) throws TransportException
	{
		((AbstractLocalTransport)to).receive(this, message);
	}

	protected final void receive(final INode from, final String message)
	{
		fireMessage(from, message);
	}
	
	@Override
	public final ITransport getTransport()
	{
		return this;
	}
}
