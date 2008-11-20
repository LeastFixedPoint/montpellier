package info.reflectionsofmind.connexion.server.remote;

import info.reflectionsofmind.connexion.transport.ClientTurnEvent;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

public interface IRemoteClient
{
	void sendStart(StartEvent start) throws ClientConnectionException;
	void sendTurn(ServerTurnEvent turn) throws ClientConnectionException;
	String getName();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		void onTurn(ClientTurnEvent event);
	}
}
