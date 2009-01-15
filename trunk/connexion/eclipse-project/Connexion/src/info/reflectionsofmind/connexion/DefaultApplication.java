package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.client.DefaultClient;
import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.server.DefaultServer;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.transport.jabber.JabberClientTransportFactory;
import info.reflectionsofmind.connexion.transport.jabber.JabberServerTransportFactory;
import info.reflectionsofmind.connexion.transport.local.LocalServerTransportFactory;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public final class DefaultApplication implements IApplication
{
	private final List<IServerTransportFactory> serverTransportFactories = new ArrayList<IServerTransportFactory>();
	private final List<IClientTransportFactory> clientTransportFactories = new ArrayList<IClientTransportFactory>();

	public DefaultApplication()
	{
		this.serverTransportFactories.add(new LocalServerTransportFactory(this));
		this.serverTransportFactories.add(new JabberServerTransportFactory(this));

		this.clientTransportFactories.add(new JabberClientTransportFactory(this));
	}

	@Override
	public IClient newClient()
	{
		return new DefaultClient(this);
	}

	@Override
	public IServer newServer()
	{
		return new DefaultServer(this);
	}

	public List<IServerTransportFactory> getServerTransportFactories()
	{
		return ImmutableList.copyOf(this.serverTransportFactories);
	}

	public List<IClientTransportFactory> getClientTransportFactories()
	{
		return ImmutableList.copyOf(this.clientTransportFactories);
	}
}
