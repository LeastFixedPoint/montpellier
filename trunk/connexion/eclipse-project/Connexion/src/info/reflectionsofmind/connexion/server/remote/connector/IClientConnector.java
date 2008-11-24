package info.reflectionsofmind.connexion.server.remote.connector;

import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

/** Clients interact through this interface to establish connection. */
public interface IClientConnector
{
	void startListening();
	void stopListening();
	
	void addListener(IListener listener);

	public interface IListener
	{
		void onConnected(IRemoteClient client);
	}
}
