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
	
	protected final void fireTrace(final String trace)
	{
		for (final IListener listener : this.listeners)
			listener.onTrace(this, trace);
	}
	
	public static abstract class Listener implements ITransport.IListener
	{
		public void onError(final ITransport transport, final String error)
		{
		}
		
		public void onPacket(final TransportNode sender, final String contents)
		{
		}
		
		public void onStarted(final ITransport transport)
		{
		}
		
		public void onStopped(final ITransport transport)
		{
		}
		
		public void onTrace(final ITransport transport, final String trace)
		{
		}
	}
}
