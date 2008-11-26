package info.reflectionsofmind.connexion.remote.client.connector;

import info.reflectionsofmind.connexion.local.server.IServer;

public class BotConnector extends AbstractClientConnector
{
	private final IServer server;

	public BotConnector(final IServer server)
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
