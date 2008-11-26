package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

/** Event: client has disconnected by its own initiative. */
public class ClientToServer_ClientDisconnectedEvent
{
	public enum Reason
	{
		CONNECTION_FAILURE, CLIENT_REQUEST
	}

	private final IRemoteClient client;
	private final Reason reason;

	public ClientToServer_ClientDisconnectedEvent(final IRemoteClient client, final Reason reason)
	{
		this.client = client;
		this.reason = reason;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}

	public Reason getReason()
	{
		return this.reason;
	}
}
