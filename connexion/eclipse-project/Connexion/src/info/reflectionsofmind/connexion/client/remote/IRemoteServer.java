package info.reflectionsofmind.connexion.client.remote;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.transport.StartEvent;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;

public interface IRemoteServer
{
	void connect(IClient client) throws ServerConnectionException, RemoteServerException;
	void sendTurn(Turn turn) throws ServerConnectionException, RemoteServerException;

	void addListener(IListener listener);

	public interface IListener
	{
		void onTurn(ServerTurnEvent event);
		void onStart(StartEvent event);
	}
}
