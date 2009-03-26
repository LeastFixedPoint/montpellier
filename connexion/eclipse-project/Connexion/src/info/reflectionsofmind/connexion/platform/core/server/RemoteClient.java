package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.AbstractSTCMessage;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_Chat;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ConnectionAccepted;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_GameStarted;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantConnected;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantDisconnected;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantStateChanged;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

public final class RemoteClient implements IRemoteClient
{
	private final Participant client;
	private final IClientNode node;
	private final IServer server;

	public RemoteClient(final IServer server, final Participant client, final IClientNode clientNode)
	{
		this.server = server;
		this.client = client;
		this.node = clientNode;
	}

	private void sendEvent(final AbstractSTCMessage event)
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
	public void sendConnectionAccepted()
	{
		sendEvent(new STCMessage_ConnectionAccepted(server));
	}

	@Override
	public void sendChatMessage(IRemoteClient client, String message)
	{
		final Integer index = (client == null) ? null : server.getClients().indexOf(client);
		sendEvent(new STCMessage_Chat(index, message));
	}

	@Override
	public void sendConnected(IRemoteClient client)
	{
		sendEvent(new STCMessage_ParticipantConnected(client.getParticipant().getName()));
	}

	@Override
	public void sendStateChanged(IRemoteClient client, State previousState)
	{
		final int clientIndex = server.getClients().indexOf(client);
		sendEvent(new STCMessage_ParticipantStateChanged(clientIndex, client.getParticipant().getState()));
	}

	@Override
	public void sendDisconnected(IRemoteClient client, DisconnectReason reason)
	{
		final int index = server.getClients().indexOf(client);
		sendEvent(new STCMessage_ParticipantDisconnected(index, reason));
	}

	@Override
	public void sendGameStarted()
	{
		sendEvent(new STCMessage_GameStarted( //
				server.getGame().getCurrentTile().getCode(), //
				server.getGame().getSequence().getTotalNumberOfTiles()));
	}

	@Override
	public void sendChange(IChange change)
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
