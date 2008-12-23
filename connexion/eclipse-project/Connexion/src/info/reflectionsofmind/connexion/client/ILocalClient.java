package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.List;

public interface ILocalClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void connect(INode serverNode);
	void sendChatMessage(String message);
	void sendLastTurn(Game game);
	void disconnect(DisconnectReason reason);

	// ====================================================================================================
	// === CLIENT GETTERS
	// ====================================================================================================

	void setName(String name);
	String getName();
	List<ITransport> getTransports();
	Settings getSettings();
	ITileSource getTileSource();

	// ====================================================================================================
	// === CONNECTED-STATE GETTERS
	// ====================================================================================================

	INode getServerNode();
	Client getClient();
	List<Client> getClients();

	// ====================================================================================================
	// === GAME-STATE GETTERS
	// ====================================================================================================

	Game getGame();
	
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
		void onChatMessage(Client sender, String message);
		void onConnectionBroken(DisconnectReason reason);
		
		void onStart();
		void onTurn(Turn turn, String nextTileCode);
	}
}
