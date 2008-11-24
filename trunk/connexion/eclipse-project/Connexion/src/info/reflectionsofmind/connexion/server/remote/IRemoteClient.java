package info.reflectionsofmind.connexion.server.remote;

import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.transport.ClientTurnEvent;
import info.reflectionsofmind.connexion.transport.ConnectionEvent;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

public interface IRemoteClient
{
	/** Server calls this when it disconnects the client. */
	void disconnect(DisconnectReason reason) throws ClientConnectionException;
	
	/** Server calls this when the game starts. */
	void sendStart(StartEvent start) throws ClientConnectionException;
	
	/** Server calls this when someone makes a turn. */
	void sendTurn(ServerTurnEvent turn) throws ClientConnectionException;
	
	String getName();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		/** Client calls this when it makes a turn. */
		void onTurn(ClientTurnEvent event);
	}
}
