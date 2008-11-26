package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;

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
	
	@Override
	public void removeListener(IListener listener)
	{
		this.listeners.remove(listener);
	}
	
	protected void fireStartEvent(ServerToClient_GameStartedEvent event)
	{
		for (IListener listener : this.listeners) listener.onStart(event);
	}
	
	protected void fireTurnEvent(ServerToClient_TurnEvent event)
	{
		for (IListener listener : this.listeners) listener.onTurn(event);
	}
}
