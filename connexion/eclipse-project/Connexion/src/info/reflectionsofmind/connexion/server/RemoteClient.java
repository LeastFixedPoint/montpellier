package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_ChatMessageEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_ClientStateChangedEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.common.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public final class RemoteClient implements IRemoteClient
{
	private final Client client;
	private final INode node;

	public RemoteClient(final Client client, final INode clientNode)
	{
		this.client = client;
		this.node = clientNode;
	}

	private void sendEvent(final ServerToClientEvent event)
	{
		try
		{
			getNode().send(event.encode());
		}
		catch (TransportException exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public void sendConnectionAccepted(IServer server)
	{
		sendEvent(new ServerToClient_ConnectionAcceptedEvent(server));
	}

	@Override
	public void sendChatMessage(IServer server, IRemoteClient client, String message)
	{
		final Integer index = (client == null) ? null : server.getClients().indexOf(client);
		sendEvent(new ServerToClient_ChatMessageEvent(index, message));
	}

	@Override
	public void sendConnected(IServer server, IRemoteClient client)
	{
		sendEvent(new ServerToClient_ClientConnectedEvent(client.getClient().getName()));
	}
	
	@Override
	public void sendStateChanged(IServer server, IRemoteClient client, State previousState)
	{
		final int clientIndex = server.getClients().indexOf(client);
		sendEvent(new ServerToClient_ClientStateChangedEvent(clientIndex, client.getClient().getState()));
	}

	@Override
	public void sendDisconnected(IServer server, IRemoteClient client, DisconnectReason reason)
	{
		final int index = server.getClients().indexOf(client);
		sendEvent(new ServerToClient_ClientDisconnectedEvent(index, reason));
	}

	@Override
	public void sendGameStarted(IServer server)
	{
		sendEvent(new ServerToClient_GameStartedEvent( //
				server.getGame().getCurrentTile().getCode(), //
				server.getGame().getSequence().getTotalNumberOfTiles()));
	}
	
	@Override
	public void sendLastTurn(IServer server)
	{
		sendEvent(new ServerToClient_TurnEvent( //
				server.getGame().getTurns().get(server.getGame().getTurns().size() - 1), //
				server.getGame().getCurrentTile().getCode()));
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	@Override
	public Client getClient()
	{
		return this.client;
	}

	@Override
	public INode getNode()
	{
		return this.node;
	}
}
