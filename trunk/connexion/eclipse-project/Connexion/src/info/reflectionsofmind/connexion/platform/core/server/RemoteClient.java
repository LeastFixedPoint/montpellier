package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.fortress.core.game.Turn;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ChatMessageEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientStateChangedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

public final class RemoteClient implements IRemoteClient
{
	private final Participant client;
	private final IClientNode node;

	public RemoteClient(final Participant client, final IClientNode clientNode)
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
		sendEvent(new ServerToClient_ClientConnectedEvent(client.getParticipant().getName()));
	}

	@Override
	public void sendStateChanged(IServer server, IRemoteClient client, State previousState)
	{
		final int clientIndex = server.getClients().indexOf(client);
		sendEvent(new ServerToClient_ClientStateChangedEvent(clientIndex, client.getParticipant().getState()));
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
		final Turn turn = server.getGame().getTurns().get(server.getGame().getTurns().size() - 1);

		sendEvent(new ServerToClient_TurnEvent( //
				turn.getLocation(), turn.getDirection(), turn.getMeepleType(), turn.getSectionIndex(), //
				server.getGame().getCurrentTile().getCode()));
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	@Override
	public Participant getParticipant()
	{
		return this.client;
	}

	@Override
	public IClientNode getNode()
	{
		return this.node;
	}
}
