package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.AbstractListener;

public abstract class AbstractServerTransport // 
		extends AbstractListener<IServerTransport.IListener> // 
		implements IServerTransport
{
	protected final void fireMessage(final IClientPacket packet)
	{
		for (final IListener listener : getListeners())
			listener.onPacket(packet);
	}

	protected final void fireError(final TransportException exception)
	{
		for (final IListener listener : getListeners())
			listener.onError(exception);
	}
}
