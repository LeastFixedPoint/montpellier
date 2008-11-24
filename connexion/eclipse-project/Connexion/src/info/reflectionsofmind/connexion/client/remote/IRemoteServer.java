package info.reflectionsofmind.connexion.client.remote;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

public interface IRemoteServer
{
	/** Client calls this when it wants to connect. */
	void connect(IClient client) throws ServerConnectionException, RemoteServerException;
	
	/** Client calls this to send a turn. */
	void sendTurn(Turn turn) throws ServerConnectionException, RemoteServerException;

	void addListener(IListener listener);

	public interface IListener
	{
		/** Server calls this to send a someone's turn. */
		void onTurn(ServerTurnEvent event);

		/** Server calls this to notify about game start. */
		void onStart(StartEvent event);
	}
}
