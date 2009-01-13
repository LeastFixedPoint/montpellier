package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.AbstractListener;

public abstract class AbstractClientTransport extends AbstractListener<IClientTransport.IListener> implements IClientTransport
{
	protected void firePacket(final String contents)
	{
		for (final IListener listener : getListeners())
			listener.onPacket(contents);
	}
}
