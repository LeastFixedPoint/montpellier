package info.reflectionsofmind.connexion.client.remote;

import info.reflectionsofmind.connexion.transport.StartEvent;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRemoteServer implements IRemoteServer
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	
	@Override
	public void addListener(IListener listener)
	{
		this.listeners.add(listener);
	}
	
	protected void fireStartEvent(StartEvent event)
	{
		for (IListener listener : this.listeners) listener.onStart(event);
	}
	
	protected void fireTurnEvent(ServerTurnEvent event)
	{
		for (IListener listener : this.listeners) listener.onTurn(event);
	}
}
