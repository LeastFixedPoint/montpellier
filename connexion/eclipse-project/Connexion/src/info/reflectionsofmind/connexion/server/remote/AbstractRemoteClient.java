package info.reflectionsofmind.connexion.server.remote;

import info.reflectionsofmind.connexion.transport.ClientTurnEvent;

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
	
	protected void fireTurnEvent(ClientTurnEvent event)
	{
		for (IListener listener : this.listeners) listener.onTurn(event);
	}
}
