package info.reflectionsofmind.connexion.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTransport implements ITransport
{
	private final List<IListener> listeners = new ArrayList<IListener>();

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
			listener.onTransportMessage(sender, message);
	}
}
