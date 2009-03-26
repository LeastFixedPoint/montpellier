package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;

public interface IRemoteClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void sendConnectionAccepted();

	void sendConnected(IRemoteClient client);
	void sendStateChanged(IRemoteClient client, State previousState);
	void sendDisconnected(IRemoteClient client, DisconnectReason reason);
	
	void sendChatMessage(IRemoteClient client, String message);
	
	void sendGameStarted();
	void sendChange(IChange change);

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	Participant getParticipant();
	IClientNode getNode();
}
