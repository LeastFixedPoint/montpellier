package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.IClient;

public interface IRemoteServer
{
	/** Call this to send a connection request. */
	void connect(IClient client) throws ServerConnectionException, RemoteServerException;
	
	/** Call this to send a turn. */
	void sendTurn(Turn turn) throws ServerConnectionException, RemoteServerException;

	/** Call this to send a message. */
	void sendMessage(String message) throws ServerConnectionException, RemoteServerException;
	
	void addListener(IListener listener);

	void removeListener(IListener listener);

	public interface IListener
	{
		/** This is called when your connection is accepted. */
		void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event);
		
		/** This is called when someone connects. */
		void onPlayerConnect(ServerToClient_PlayerConnectedEvent event);
		
		/** This is called when someone disconnects. */
		void onPlayerDisconnect(ServerToClient_PlayerDisconnectedEvent event);
		
		/** This is called when someone makes a turn. */
		void onTurn(ServerToClient_TurnEvent event);

		/** This is called when the game starts. */
		void onStart(ServerToClient_GameStartedEvent event);
		
		/** Client calls this when it send a message. */
		void onMessage(ServerToClient_MessageEvent event);
	}
}
