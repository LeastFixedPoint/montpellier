package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;

public abstract class AbstractLocalTransport extends AbstractTransport
{
	@Override
	public String getName()
	{
		return "Local";
	}

	protected void receive(final AbstractLocalNode from, final String message)
	{
		fireMessage(from, message);
	}

	public interface AbstractLocalNode extends INode
	{
		AbstractLocalTransport getTransport();
	}
}
