package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport;

import java.util.List;

public interface ILocalClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	/** Will {@link ClientLocalTransport#start()} the transport. */
	void connect(ClientLocalTransport transport);

	/** Will {@link ClientLocalTransport#stop()} the transport. */
	void disconnect(DisconnectReason reason);

	void sendChatMessage(String message);
	void sendLastTurn();
	
	// ====================================================================================================
	// === CLIENT GETTERS
	// ====================================================================================================

	void setName(String name);
	String getName();
	ITileSource getTileSource();

	// ====================================================================================================
	// === CONNECTED-STATE GETTERS
	// ====================================================================================================

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
