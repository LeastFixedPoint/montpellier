package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.common.InitializationException;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class TransportManager
{
	private final List<ITransportFactory> transportFactories = new ArrayList<ITransportFactory>();

	public TransportManager() throws InitializationException
	{
		this.transportFactories.add(new JabberTransportFactory());
		this.transportFactories.add(new LocalTransportFactory());
		this.transportFactories.add(new BotTransportFactory());
	}

	public List<ITransportFactory> getTransportFactories()
	{
		return ImmutableList.copyOf(this.transportFactories);
	}
}
