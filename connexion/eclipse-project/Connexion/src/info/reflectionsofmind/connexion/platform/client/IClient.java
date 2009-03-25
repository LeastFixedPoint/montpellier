package info.reflectionsofmind.connexion.platform.client;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.common.Participant;
import info.reflectionsofmind.connexion.platform.transport.IClientTransport;
import info.reflectionsofmind.connexion.platform.transport.local.LocalClientTransport;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.util.List;

public interface IClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	/** Will not {@link IClientTransport#start()} the transport. */
	void connect(IClientTransport transport);

	/** Will not {@link LocalClientTransport#stop()} the transport. */
	void disconnect(DisconnectReason reason);

	void sendChatMessage(String message);

	void sendTurn(Turn turn);

	// ====================================================================================================
	// === CLIENT GETTERS
	// ====================================================================================================

	void setName(String name);

	String getName();

	ITileSource getTileSource();

	IApplication getApplication();

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

		void onAfterConnectionBroken(DisconnectReason reason);

		void onStart();

		void onTurn(Turn turn, String nextTileCode);
	}
}
