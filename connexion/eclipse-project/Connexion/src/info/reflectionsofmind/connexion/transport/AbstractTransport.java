package info.reflectionsofmind.connexion.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTransport implements ITransport
{
	private int connections = 0;

	private final List<IListener> listeners = new ArrayList<IListener>();

	@Override
	public final void start() throws TransportException
	{
		if (this.connections == 0) doFirstStart();
		this.connections++;
		doStart();
	}

	protected void doStart() throws TransportException
	{
	}

	protected void doFirstStart() throws TransportException
	{
	}

	@Override
	public void stop() throws TransportException
	{
		try
		{
			doStop();
		}
		finally
		{
			this.connections--;
			if (this.connections == 0) doFinalStop();
		}
	}

	protected void doStop() throws TransportException
	{
	}

	protected void doFinalStop() throws TransportException
	{
	}

	public final void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	public final void removeListener(final IListener listener)
	{
		this.listeners.remove(listener);
	}

	protected void fireMessage(final INode sender, final String message)
	{
		for (final IListener listener : this.listeners)
			listener.onMessage(sender, message);
	}
}
