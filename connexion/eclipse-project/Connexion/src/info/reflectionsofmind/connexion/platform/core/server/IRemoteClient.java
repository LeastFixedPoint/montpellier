package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;

public interface IRemoteClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void sendConnectionAccepted(IServer server);

	void sendConnected(IServer server, IRemoteClient client);
	void sendStateChanged(IServer server, IRemoteClient client, State previousState);
	void sendDisconnected(IServer server, IRemoteClient client, DisconnectReason reason);
	
	void sendChatMessage(IServer server, IRemoteClient client, String message);
	
	void sendGameStarted(IServer server);
	void sendLastTurn(IServer server);

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	Participant getParticipant();
	IClientNode getNode();
}
