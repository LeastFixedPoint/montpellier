package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.transport.INode;

import java.util.List;

public interface IRemoteServer
{
	// ============================================================================================
	// === COMMANDS
	// ============================================================================================
	
	void disconnect(DisconnectReason reason);
	void sendConnectionRequest(IClient client);
	void sendChatMessage(String message);
	void sendLastTurn(Game game);

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	List<Client> getClients();
	INode getServerNode();
	
	// ============================================================================================
	// === LISTENERS
	// ============================================================================================

	void addListener(IListener listener);
	
	public interface IListener
	{
		void onConnectionAccepted();
		void onClientConnected(Client client);
		void onClientDisconnected(Client client);
		void onClientStateChanged(Client client);
		void onChatMessage(Client sender, String message);
		void onConnectionBroken(DisconnectReason reason);
		void onTurn(Turn turn, String nextTileCode);
	}
}
