package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public final class RemoteClient<TransportType extends ITransport<NodeType>, NodeType extends INode> // 
		implements IRemoteClient<TransportType, NodeType>
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final String name;
	private final TransportType transport;
	private final NodeType clientNode;

	public RemoteClient(final TransportType transport, final NodeType clientNode, final String name)
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
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public NodeType getClientNode()
	{
		return this.clientNode;
	}
}
