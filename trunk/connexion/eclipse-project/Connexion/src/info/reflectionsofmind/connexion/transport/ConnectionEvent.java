package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

public class ConnectionEvent
{
	private final IRemoteClient client;

	public ConnectionEvent(final IRemoteClient client)
	{
		this.client = client;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}
}
