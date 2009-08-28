package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.AbstractSTCMessage;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_Change;
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
	private IPlayer player;

	public RemoteClient(final IServer server, final Participant client, final IClientNode clientNode)
	{
		this.server = server;
		this.client = client;
		this.node = clientNode;
	}

	private void sendMessage(final AbstractSTCMessage event)
	{
		try
		{
			getNode().send(event.encode());
		}
		catch (final TransportException exception)
		{
			exception.printStackTrace();
		}
	}

	@Override
	public void sendConnectionAccepted()
	{
		sendMessage(new STCMessage_ConnectionAccepted(this.server));
	}

	@Override
	public void sendChatMessage(final IRemoteClient client, final String message)
	{
		final Integer index = client == null ? null : this.server.getClients().indexOf(client);
		sendMessage(new STCMessage_Chat(index, message));
	}

	@Override
	public void sendConnected(final IRemoteClient client)
	{
		sendMessage(new STCMessage_ParticipantConnected(client.getParticipant().getName()));
	}

	@Override
	public void sendStateChanged(final IRemoteClient client, final State previousState)
	{
		final int clientIndex = this.server.getClients().indexOf(client);
		sendMessage(new STCMessage_ParticipantStateChanged(clientIndex, client.getParticipant().getState()));
	}

	@Override
	public void sendDisconnected(final IRemoteClient client, final DisconnectReason reason)
	{
		final int index = this.server.getClients().indexOf(client);
		sendMessage(new STCMessage_ParticipantDisconnected(index, reason));
	}

	@Override
	public void sendGameStarted()
	{
		final IStartInfo startInfo = this.server.getGame().getClientStartInfo(this.player);
		final String string = this.server.getGame().getStartInfoCoder().encode(startInfo);
		sendMessage(new STCMessage_GameStarted(string));
	}

	@Override
	public void sendChange(final IChange change)
	{
		sendMessage(new STCMessage_Change(this.server.getGame().getChangeCoder().encode(change)));
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
