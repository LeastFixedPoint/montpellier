package info.reflectionsofmind.connexion.local.client.gui.join;

import info.reflectionsofmind.connexion.local.client.gui.join.JoinGameDialog.IConnector;
import info.reflectionsofmind.connexion.transport.ITransport;

public class ServerType
{
	private final String name;
	private final ITransport<?> transport;
	private final JoinGameDialog.IConnector connector;

	public ServerType(final String name, //
			final ITransport<?> transport, // 
			final IConnector connector)
	{
		this.name = name;
		this.transport = transport;
		this.connector = connector;
	}

	public String getName()
	{
		return this.name;
	}

	public ITransport<?> getTransport()
	{
		return this.transport;
	}

	public JoinGameDialog.IConnector getConnector()
	{
		return this.connector;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
