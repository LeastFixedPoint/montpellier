package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.IServerToClientEventListener;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEventDecoder;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public class RemoteServer implements IRemoteServer, ITransport.IListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final INode serverNode;

	public RemoteServer(final INode serverNode)
	{
		this.serverNode = serverNode;
		getServerNode().getTransport().addListener(this);
	}

	private void sendEvent(ClientToServerEvent event)
	{
		try
		{
			getServerNode().getTransport().send(getServerNode(), event.encode());
		}
		catch (final TransportException exception)
		{
			disconnect(DisconnectReason.CONNECTION_FAILURE);
		}
	}
	
	@Override
	public void disconnect(DisconnectReason reason)
	{
	}

	@Override
	public void sendConnectionRequest(IClient client)
	{
		sendEvent(new ClientToServer_ClientConnectionRequestEvent(client.getName()));
	}

	@Override
	public void sendChatMessage(String message)
	{
		sendEvent(new ClientToServer_ChatMessageEvent(message));
	}

	@Override
	public void sendLastTurn(Game game)
	{
		final Turn lastTurn = game.getTurns().get(game.getTurns().size() - 1);
		sendEvent(new ClientToServer_TurnEvent(lastTurn));
	}

	public void stop() throws ServerConnectionException
	{
		try
		{
			getServerNode().getTransport().stop();
		}
		catch (final TransportException exception)
		{
			throw new ServerConnectionException(exception);
		}
	}

	@Override
	public void onTransportMessage(INode from, String message)
	{
		ServerToClientEvent event = ServerToClientEventDecoder.decode(message);

		for (IServerToClientEventListener listener : this.listeners)
		{
			event.dispatch(listener);
		}
	}

	@Override
	public void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(final IServerToClientEventListener listener)
	{
		this.listeners.remove(listener);
	}

	private INode getServerNode()
	{
		return this.serverNode;
	}
}
