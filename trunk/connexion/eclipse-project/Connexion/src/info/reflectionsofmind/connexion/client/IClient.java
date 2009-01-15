package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.common.Participant;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport;

import java.util.List;

public interface IClient
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

	Participant getParticipant();
	List<Participant> getParticipants();

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
		void onClientConnected(Participant client);
		void onClientDisconnected(Participant client);
		void onChatMessage(Participant sender, String message);
		void onConnectionBroken(DisconnectReason reason);
		
		void onStart();
		void onTurn(Turn turn, String nextTileCode);
	}
}
