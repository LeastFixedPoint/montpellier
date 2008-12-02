package info.reflectionsofmind.connexion.local.server.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTransport implements ITransport
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	
	public final void addListener(IListener listener)
	{
		this.listeners.add(listener);
	}

	protected void fireMessage(ISender sender, String message)
	{
		for (IListener listener : this.listeners)
			listener.onMessage(sender, message);
	}
}
