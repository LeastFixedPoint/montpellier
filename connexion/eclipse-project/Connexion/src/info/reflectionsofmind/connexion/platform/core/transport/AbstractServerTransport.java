package info.reflectionsofmind.connexion.platform.core.transport;

import info.reflectionsofmind.connexion.util.AbstractListener;

public abstract class AbstractServerTransport // 
		extends AbstractListener<IServerTransport.IListener> // 
		implements IServerTransport
{
	@Override
	public void stop()
	{
		fireBeforeStopped(this);
	}

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

	protected final void fireBeforeStopped(IServerTransport transport)
	{
		for (final IListener listener : getListeners())
			listener.onBeforeStopped(transport);
	}
}
