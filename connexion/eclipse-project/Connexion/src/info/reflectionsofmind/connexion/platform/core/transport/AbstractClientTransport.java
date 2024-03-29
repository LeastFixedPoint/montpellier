package info.reflectionsofmind.connexion.platform.core.transport;

import info.reflectionsofmind.connexion.util.AbstractListener;

public abstract class AbstractClientTransport extends AbstractListener<IClientToServerTransport.IListener> implements IClientToServerTransport
{
	protected void firePacket(final String contents)
	{
		for (final IListener listener : getListeners())
		{
			listener.onPacket(contents);
		}
	}
}
