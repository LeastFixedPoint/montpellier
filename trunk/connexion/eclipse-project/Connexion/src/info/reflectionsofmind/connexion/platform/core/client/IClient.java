package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGame;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.transport.IClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.local.LocalClientTransport;

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

	void sendAction(IAction action);

	// ====================================================================================================
	// === CLIENT GETTERS
	// ====================================================================================================

	void setName(String name);

	String getName();

	IApplication getApplication();

	// ====================================================================================================
	// === CONNECTED-STATE GETTERS
	// ====================================================================================================

	Participant getParticipant();

	List<Participant> getParticipants();

	// ====================================================================================================
	// === GAME-STATE GETTERS
	// ====================================================================================================

	IClientGame<?, ?, ?, ?> getGame();

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

		void onGameStarting();
	}
}
