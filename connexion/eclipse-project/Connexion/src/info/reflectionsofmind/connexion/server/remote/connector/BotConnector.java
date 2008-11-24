package info.reflectionsofmind.connexion.server.remote.connector;

import info.reflectionsofmind.connexion.server.local.IServer;

public class BotConnector extends AbstractClientConnector
{
	private final IServer server;

	public BotConnector(final IServer server)
	{
		this.server = server;
	}
	
	@Override
	public void listen()
	{
	}
	
	@Override
	public void disconnect()
	{
	}
}
