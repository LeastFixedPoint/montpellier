package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.transport.TransportException;

public interface IRemoteServer
{
	void start() throws TransportException;
	
	void stop() throws TransportException;
	
	void sendEvent(ClientToServerEvent event) throws ServerConnectionException, RemoteServerException;

	void addListener(IListener listener);

	void removeListener(IListener listener);

	public interface IListener
	{
		/** This is called when your connection is accepted. */
		void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event);

		/** This is called when someone connects. */
		void onPlayerConnected(ServerToClient_PlayerConnectedEvent event);

		/** This is called when someone disconnects. */
		void onPlayerDisconnected(ServerToClient_PlayerDisconnectedEvent event);

		/** This is called when someone makes a turn. */
		void onTurn(ServerToClient_TurnEvent event);

		/** This is called when the game starts. */
		void onStart(ServerToClient_GameStartedEvent event);

		/** Client calls this when it send a message. */
		void onMessage(ServerToClient_MessageEvent event);
	}
}
