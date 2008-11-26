package info.reflectionsofmind.connexion.remote.client.connector;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClientConnector implements IClientConnector
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	
	@Override
	public void addListener(IListener listener)
	{
		listeners.add(listener);
	}
	
	protected void fireConnected(IRemoteClient client)
	{
		for (IListener listener : this.listeners)
		{
			listener.onConnected(client);
		}
	}
}
