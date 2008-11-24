package info.reflectionsofmind.connexion.server.remote.connector;

import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

/** Clients interact through this interface to establish connection. */
public interface IClientConnector
{
	void listen();
	void disconnect();
	
	void addListener(IListener listener);

	public interface IListener
	{
		void onConnected(IRemoteClient client);
	}
}
