package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.local.client.ILocalClient;
import info.reflectionsofmind.connexion.transport.INode;

public interface IRemoteServer
{
	// ============================================================================================
	// === COMMANDS
	// ============================================================================================
	
	void sendConnectionRequest();
	void sendChatMessage(String message);
	void sendLastTurn(Game game);
	void disconnect(DisconnectReason reason);

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	INode getServerNode();
	ILocalClient getLocalClient();
	
	// ============================================================================================
	// === LISTENERS
	// ============================================================================================

	void addListener(IListener listener);
	void removeListener(IListener listener);
	
	public interface IListener
	{
		void onConnectionAccepted();
		void onClientConnected(Client client);
		void onClientDisconnected(Client client);
		void onClientStateChanged(Client client);
		void onChatMessage(Client sender, String message);
		void onConnectionBroken(DisconnectReason reason);
		
		void onStart();
		void onTurn(Turn turn, String nextTileCode);
	}
}
