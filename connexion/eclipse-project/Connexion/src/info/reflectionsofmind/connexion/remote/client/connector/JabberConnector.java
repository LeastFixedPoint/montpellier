package info.reflectionsofmind.connexion.remote.client.connector;

import info.reflectionsofmind.connexion.local.server.IServer;

public class JabberConnector extends AbstractClientConnector
{
	private final IServer server;

	public JabberConnector(final IServer server)
	{
		this.server = server;
	}

	@Override
	public void startListening()
	{
		
	}

	@Override
	public void stopListening()
	{
	}
}
