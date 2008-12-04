package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public class RemoteServer<TransportType extends ITransport<NodeType>, NodeType extends INode> //
		implements IRemoteServer
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final TransportType transport;
	private final NodeType serverNode;

	public RemoteServer(final TransportType transport, final NodeType serverNode)
	{
		this.transport = transport;
		this.serverNode = serverNode;
	}

	public void start() throws TransportException
	{
		this.transport.start();
	}

	public void stop() throws TransportException
	{
		this.transport.stop();
	}

	@Override
	public void sendEvent(final ClientToServerEvent event) throws ServerConnectionException
	{
		try
		{
			this.transport.send(this.serverNode, event.encode());
		}
		catch (TransportException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}

	@Override
	public void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IListener listener)
	{
		this.listeners.remove(listener);
	}
}
