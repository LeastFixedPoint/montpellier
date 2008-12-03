package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.transport.TransportException;

public interface IRemoteClient
{
	void sendEvent(ServerToClientEvent event) throws TransportException;
	
	String getName();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		/** Client calls this when it wants to connect. */
		void onConnectionRequest(IRemoteClient sender, ClientToServer_ClientConnectionRequestEvent event);
		
		/** Client calls this before it disconnects. */
		void onDisconnect(IRemoteClient sender, ClientToServer_ClientDisconnectedEvent event);
		
		/** Client calls this when it makes a turn. */
		void onTurn(IRemoteClient sender, ClientToServer_TurnEvent event);
		
		/** Client calls this when it send a message. */
		void onMessage(IRemoteClient sender, ClientToServer_MessageEvent event);
	}
}
