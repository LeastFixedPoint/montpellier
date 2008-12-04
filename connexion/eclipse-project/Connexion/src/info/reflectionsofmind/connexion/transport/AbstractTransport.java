package info.reflectionsofmind.connexion.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTransport<SenderType extends INode> implements ITransport<SenderType>
{
	private int connections = 0;

	private final List<IListener<SenderType>> listeners = new ArrayList<IListener<SenderType>>();

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

	public final void addListener(final IListener<SenderType> listener)
	{
		this.listeners.add(listener);
	}

	public final void removeListener(final IListener<SenderType> listener)
	{
		this.listeners.remove(listener);
	}

	protected void fireMessage(final SenderType sender, final String message)
	{
		for (final IListener<SenderType> listener : this.listeners)
			listener.onMessage(sender, message);
	}
}
