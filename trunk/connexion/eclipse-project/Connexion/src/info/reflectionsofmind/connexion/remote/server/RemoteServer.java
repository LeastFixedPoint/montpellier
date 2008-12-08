package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClientDecoder;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public class RemoteServer<TransportType extends ITransport, NodeType extends INode> //
		implements IRemoteServer, ITransport.IListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final TransportType transport;
	private final NodeType serverNode;

	public RemoteServer(final TransportType transport, final NodeType serverNode)
	{
		this.transport = transport;
		this.serverNode = serverNode;
		this.transport.addListener(this);
	}

	public void start() throws ServerConnectionException
	{
		try
		{
			this.transport.start();
		}
		catch (final TransportException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}

	public void stop() throws ServerConnectionException
	{
		try
		{
			this.transport.stop();
		}
		catch (final TransportException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}
	
	@Override
	public void onMessage(INode from, String message)
	{
		ServerToClientEvent event = ServerToClientDecoder.decode(message);
		
		for (IListener listener : this.listeners)
		{
			event.dispatch(listener);
		}
	}

	@Override
	public void sendEvent(final ClientToServerEvent event) throws ServerConnectionException
	{
		try
		{
			this.transport.send(this.serverNode, event.encode());
		}
		catch (final TransportException exception)
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
	public void removeListener(final IListener listener)
	{
		this.listeners.remove(listener);
	}
	
	public TransportType getTransport()
	{
		return this.transport;
	}
}
