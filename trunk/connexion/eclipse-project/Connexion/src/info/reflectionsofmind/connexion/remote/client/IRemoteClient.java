package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public interface IRemoteClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void sendEvent(ServerToClientEvent event) throws TransportException;
	
	void sendConnected(IServer server, IRemoteClient client);
	void sendStateChanged(IServer server, IRemoteClient client, State previousState);
	void sendDisconnected(IServer server, IRemoteClient client, DisconnectReason reason);
	
	void sendChatMessage(IServer server, IRemoteClient client, String message);
	
	void sendGameStarted(IServer server);
	void sendLastTurn(IServer server);

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	Client getClient();
	INode getNode();
}
