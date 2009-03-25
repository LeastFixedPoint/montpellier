package info.reflectionsofmind.connexion.platform.core.transport.local;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.core.transport.AbstractServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.DefaultClientPacket;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

public class LocalServerTransport extends AbstractServerTransport
{
	private final IApplication application;
	private final int numberOfPlayers;

	public LocalServerTransport(final IApplication application, final int numberOfPlayers)
	{
		this.application = application;
		this.numberOfPlayers = numberOfPlayers;
	}
	
	@Override
	public String getName()
	{
		return "Local";
	}

	public void receive(LocalClientNode node, String contents)
	{
		System.out.format("[%s] => [%s]: %s\n", node.getClientTransport().getName(), "Server", contents);
		fireMessage(new DefaultClientPacket(node, contents));
	}

	@Override
	public void start() throws TransportException
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			new LocalClientTransport(this, i).display();
		}
	}

	public IApplication getApplication()
	{
		return this.application;
	}
}
