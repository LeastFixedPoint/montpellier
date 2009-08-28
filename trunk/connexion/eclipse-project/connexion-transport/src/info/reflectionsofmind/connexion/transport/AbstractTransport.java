package info.reflectionsofmind.connexion.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTransport implements ITransport
{
	private final List<ITransport.IListener> listeners = new ArrayList<IListener>();
	
	public final void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}
	
	protected final void fireStarted()
	{
		for (final IListener listener : this.listeners)
			listener.onStarted(this);
	}
	
	protected final void fireStopped()
	{
		for (final IListener listener : this.listeners)
			listener.onStopped(this);
	}
	
	protected final void firePacket(final TransportNode sender, final String contents)
	{
		for (final IListener listener : this.listeners)
			listener.onPacket(sender, contents);
	}
	
	protected final void fireError(final String error)
	{
		for (final IListener listener : this.listeners)
			listener.onError(this, error);
	}
	
	public static abstract class Listener implements ITransport.IListener
	{
		@Override
		public void onError(final ITransport transport, final String error)
		{
		}
		
		@Override
		public void onPacket(final TransportNode sender, final String contents)
		{
		}
		
		@Override
		public void onStarted(final ITransport transport)
		{
		}
		
		@Override
		public void onStopped(final ITransport transport)
		{
		}
	}
}
