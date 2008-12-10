package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerRejectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;

public interface IRemoteServer
{
	void start() throws ServerConnectionException;
	
	void stop() throws ServerConnectionException;
	
	void sendEvent(ClientToServerEvent event) throws ServerConnectionException, RemoteServerException;

	void addListener(IListener listener);

	void removeListener(IListener listener);

	public interface IListener
	{  
		/** This is called when your connection is accepted. */
		void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event);
		
		/** This is called when someone connects. */
		void onClientConnected(ServerToClient_ClientConnectedEvent event);

		/** This is called when someone disconnects. */
		void onClientDisconnected(ServerToClient_ClientDisconnectedEvent event);

		/** This is called when somebody is accepted into game as player or spectator. */
		void onParticipantAccepted(ServerToClient_PlayerAcceptedEvent event);

		/** This is called when somebody is rejected from game. */
		void onParticipantRejected(ServerToClient_PlayerRejectedEvent event);

		/** This is called when someone makes a turn. */
		void onTurn(ServerToClient_TurnEvent event);

		/** This is called when the game starts. */
		void onStart(ServerToClient_GameStartedEvent event);

		/** Client calls this when it send a message. */
		void onMessage(ServerToClient_MessageEvent event);
	}
}
