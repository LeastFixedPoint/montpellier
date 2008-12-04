package info.reflectionsofmind.connexion.transport.hotseat;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;

public abstract class AbstractHotSeatTransport extends AbstractTransport<AbstractHotSeatTransport.AbstractHotSeatNode>
{
	@Override
	public String getName()
	{
		return "Local";
	}

	protected void receive(final AbstractHotSeatNode from, final String message)
	{
		fireMessage(from, message);
	}

	public interface AbstractHotSeatNode extends INode
	{
		AbstractHotSeatTransport getTransport();
	}
}
