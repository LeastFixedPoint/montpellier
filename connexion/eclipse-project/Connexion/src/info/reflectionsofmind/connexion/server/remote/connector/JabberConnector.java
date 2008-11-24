package info.reflectionsofmind.connexion.server.remote.connector;

import info.reflectionsofmind.connexion.server.local.IServer;

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
