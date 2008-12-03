package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.transport.IAddressee;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRemoteClient implements IRemoteClient
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final ITransport<IAddressee> transport;
	private final IAddressee clientAddressee;

	public AbstractRemoteClient(ITransport<IAddressee> transport, IAddressee clientAddressee)
	{
		this.transport = transport;
		this.clientAddressee = clientAddressee;
	}

	@Override
	public void addListener(IListener listener)
	{
		this.listeners.add(listener);
	}

	public void sendEvent(ServerToClientEvent event) throws TransportException
	{
		this.transport.send(this.clientAddressee, event.encode());
	}

	protected void fireTurnEvent(ClientToServer_TurnEvent event)
	{
		for (IListener listener : this.listeners)
		{
			listener.onTurn(this, event);
		}
	}
}
