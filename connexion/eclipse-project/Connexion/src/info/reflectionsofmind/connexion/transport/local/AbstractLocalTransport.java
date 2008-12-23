package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public abstract class AbstractLocalTransport extends AbstractTransport
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
	public INode getNode(final String id)
	{
		throw new UnsupportedOperationException();
	}

	public void receive(final INode from, final String message)
	{
		fireMessage(from, message);
	}
}
