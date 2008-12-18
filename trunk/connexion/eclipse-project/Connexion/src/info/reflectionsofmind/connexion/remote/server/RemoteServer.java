package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.IServerToClientEventListener;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEventDecoder;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ChatMessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientStateChangedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.ILocalClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RemoteServer implements IRemoteServer, ITransport.IListener, IServerToClientEventListener
{
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final List<Client> clients = new ArrayList<Client>();

	private final INode serverNode;
	private final ILocalClient localClient;

	public RemoteServer(final ILocalClient localClient, final INode serverNode)
	{
		this.serverNode = serverNode;
		this.localClient = localClient;

		getServerNode().getTransport().addListener(this);
	}

	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	private void sendEvent(final ClientToServerEvent event)
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
	public void disconnect(final DisconnectReason reason)
	{
	}

	@Override
	public void sendConnectionRequest()
	{
		sendEvent(new ClientToServer_ClientConnectionRequestEvent(getLocalClient().getName()));
	}

	@Override
	public void sendChatMessage(final String message)
	{
		sendEvent(new ClientToServer_ChatMessageEvent(message));
	}

	@Override
	public void sendLastTurn(final Game game)
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

	// ====================================================================================================
	// === TRANSPORT LISTENER
	// ====================================================================================================

	@Override
	public void onTransportMessage(final INode from, final String message)
	{
		ServerToClientEventDecoder.decode(message).dispatch(this);
	}

	// ====================================================================================================
	// === STC EVENT DISPATCH TARGET
	// ====================================================================================================

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		final Iterator<String> names = event.getExistingPlayers().iterator();
		final Iterator<State> states = event.getStates().iterator();

		while (names.hasNext() && states.hasNext())
		{
			this.clients.add(new Client(names.next(), states.next()));
		}

		for (final IListener listener : this.listeners)
		{
			listener.onConnectionAccepted();
		}
	}

	@Override
	public void onClientConnected(final ServerToClient_ClientConnectedEvent event)
	{
		final Client newClient = new Client(event.getClientName());
		this.clients.add(newClient);

		for (final IListener listener : this.listeners)
		{
			listener.onClientConnected(newClient);
		}
	}

	@Override
	public void onClientStateChanged(final ServerToClient_ClientStateChangedEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());
		client.setState(event.getNewState());
	}

	@Override
	public void onClientDisconnected(final ServerToClient_ClientDisconnectedEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());
		this.clients.remove(client);

		for (final IListener listener : this.listeners)
		{
			listener.onClientDisconnected(client);
		}
	}

	@Override
	public void onChatMessage(final ServerToClient_ChatMessageEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());

		for (final IListener listener : this.listeners)
		{
			listener.onChatMessage(client, event.getMessage());
		}
	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{

	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{

	}

	// ====================================================================================================
	// === SELF-LISTENERS
	// ====================================================================================================

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

	public INode getServerNode()
	{
		return this.serverNode;
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	public ILocalClient getLocalClient()
	{
		return this.localClient;
	}
}
