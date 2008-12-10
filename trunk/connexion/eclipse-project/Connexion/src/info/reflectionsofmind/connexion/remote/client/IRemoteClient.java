package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

/**
 * <ul>
 * <li><b>WILL</b> react to commands and notify state listeners.</li>
 * </ul>
 */
public interface IRemoteClient
{
	public enum State
	{
		PENDING, CONNECTED, ACCEPTED, SPECTATOR, DISCONNECTED;

		public static boolean isConnected(State state)
		{
			return (state == CONNECTED) || (state == ACCEPTED) || (state == SPECTATOR);
		}
	};

	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void sendEvent(ServerToClientEvent event) throws TransportException;

	void setConnected();

	void setAcceptedAs(Player player);

	void setRejected();

	void setDisconnected(DisconnectReason disconnectReason);

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	String getName();

	INode getNode();

	State getState();

	Player getPlayer();

	// ====================================================================================================
	// === LISTENERS
	// ====================================================================================================

	void addListener(IStateListener listener);

	public interface IStateListener
	{
		/** This is called when client state changes. */
		void onAfterClientStateChange(IRemoteClient client, State previousState);
	}
}
