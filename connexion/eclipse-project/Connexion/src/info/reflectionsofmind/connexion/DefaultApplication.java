package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.client.DefaultClient;
import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.server.DefaultServer;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.transport.IServerTransportFactory;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class DefaultApplication implements IApplication
{
	private final List<IServerTransportFactory> serverTransportFactories;
	private final List<IClientTransportFactory> clientTransportFactories;

	public DefaultApplication()
	{

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
