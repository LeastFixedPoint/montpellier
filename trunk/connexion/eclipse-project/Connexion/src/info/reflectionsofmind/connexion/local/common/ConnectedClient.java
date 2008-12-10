package info.reflectionsofmind.connexion.local.common;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

public class ConnectedClient
{
	private final IRemoteClient client;

	public ConnectedClient(IRemoteClient client)
	{
		this.client = client;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}
}
