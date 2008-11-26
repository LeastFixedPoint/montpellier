package info.reflectionsofmind.connexion.remote.client.connector;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

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
