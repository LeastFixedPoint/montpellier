package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.transport.AbstractServerTransport;
import info.reflectionsofmind.connexion.transport.DefaultClientPacket;
import info.reflectionsofmind.connexion.transport.TransportException;

public class LocalServerTransport extends AbstractServerTransport
{
	private final IApplication application;

	public LocalServerTransport(final IApplication application, final int numberOfPlayers)
	{
		this.application = application;
	}

	public void receive(LocalClientNode node, String contents)
	{
		fireMessage(new DefaultClientPacket(node, contents));
	}

	@Override
	public void start() throws TransportException
	{
		new LocalClientTransport(this, 0).start();
	}

	@Override
	public void stop()
	{
	}

	public IApplication getApplication()
	{
		return this.application;
	}
}
