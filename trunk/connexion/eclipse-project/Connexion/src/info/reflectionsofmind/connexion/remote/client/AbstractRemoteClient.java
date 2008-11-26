package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRemoteClient implements IRemoteClient
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	
	@Override
	public void addListener(IListener listener)
	{
		this.listeners.add(listener);
	}
	
	protected void fireTurnEvent(ClientToServer_TurnEvent event)
	{
		for (IListener listener : this.listeners) listener.onTurn(this, event);
	}
}
