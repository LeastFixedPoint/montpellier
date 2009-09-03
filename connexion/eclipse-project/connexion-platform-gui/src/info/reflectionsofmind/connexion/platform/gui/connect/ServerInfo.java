package info.reflecitonsofmind.connexion.platform.gui.connect;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;
import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.INamed;

public final class ServerInfo implements INamed
{
	private final String name;
	private final IConnectionParameters transportConnectionParameters;
	private final String serverAddress;
	private final ITransportFactory factory;
	
	public ServerInfo(final String name, final ITransportFactory factory,
			final IConnectionParameters transportConnectionParameters, final String serverAddress)
	{
		this.name = name;
		this.factory = factory;
		this.transportConnectionParameters = transportConnectionParameters;
		this.serverAddress = serverAddress;
	}
	
	public IConnectionParameters getParameters()
	{
		return this.transportConnectionParameters;
	}
	
	public String getAddress()
	{
		return this.serverAddress;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public ITransportFactory getFactory()
	{
		return this.factory;
	}
	
	@Override
	public String toString()
	{
		return getName() + " (" + getFactory().getName() + ")";
	}
}
