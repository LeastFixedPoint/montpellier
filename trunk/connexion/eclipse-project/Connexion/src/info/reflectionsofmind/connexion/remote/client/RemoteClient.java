package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public final class RemoteClient implements IRemoteClient, ITransport.IListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final String name;
	private final ITransport transport;
	private final INode clientNode;

	public RemoteClient(final ITransport transport, final INode clientNode, final String name)
	{
		this.name = name;
		this.transport = transport;
		this.clientNode = clientNode;
	}

	@Override
	public void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void sendEvent(final ServerToClientEvent event) throws TransportException
	{
		this.transport.send(this.clientNode, event.encode());
	}

	@Override
	public void onMessage(INode from, String message)
	{
		
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public INode getClientNode()
	{
		return this.clientNode;
	}
}
