package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public interface IRemoteClient
{
	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	void sendEvent(ServerToClientEvent event) throws TransportException;

	void setConnected();

	void setAccepted();

	void setSpectator();

	void setRejected();

	void setDisconnected(DisconnectReason disconnectReason);

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	String getName();

	INode getNode();

	State getState();

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
