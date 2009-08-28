package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.fortress.core.common.FortressGameFactory;
import info.reflectionsofmind.connexion.platform.core.client.DefaultClient;
import info.reflectionsofmind.connexion.platform.core.client.IClient;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGameFactory;
import info.reflectionsofmind.connexion.platform.core.server.DefaultServer;
import info.reflectionsofmind.connexion.platform.core.server.IServer;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.core.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.jabber.JabberClientTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.jabber.JabberServerTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.local.LocalServerTransportFactory;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public final class DefaultApplication implements IApplication
{
	private final List<IServerTransportFactory> serverTransportFactories = new ArrayList<IServerTransportFactory>();
	private final List<IClientTransportFactory> clientTransportFactories = new ArrayList<IClientTransportFactory>();

	private final List<IServerGameFactory> serverGameFactories = new ArrayList<IServerGameFactory>();
	private final List<IClientGameFactory> clientGameFactories = new ArrayList<IClientGameFactory>();

	public DefaultApplication()
	{
		this.serverTransportFactories.add(new LocalServerTransportFactory(this));
		this.serverTransportFactories.add(new JabberServerTransportFactory(this));
		this.clientTransportFactories.add(new JabberClientTransportFactory(this));

		final FortressGameFactory fortressGameFactory = new FortressGameFactory();
		this.serverGameFactories.add(fortressGameFactory);
		this.clientGameFactories.add(fortressGameFactory);
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
