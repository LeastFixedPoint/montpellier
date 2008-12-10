package info.reflectionsofmind.connexion.local.common;

public class SpectatingClient
{
	private final ConnectedClient client;

	public SpectatingClient(ConnectedClient client)
	{
		this.client = client;
	}

	public ConnectedClient getConnectedClient()
	{
		return this.client;
	}
}
