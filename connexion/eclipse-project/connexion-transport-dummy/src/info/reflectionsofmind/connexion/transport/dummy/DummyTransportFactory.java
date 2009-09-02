package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.ITransportFactory;

public final class DummyTransportFactory implements ITransportFactory
{
	public ITransport createTransport(final IConnectionParameters parameters)
	{
		return new DummyTransport(((DummyConnectionParameters) parameters).getNumberOfPlayers());
	}
	
	@Override
	public String getName()
	{
		return "Dummy";
	}
	
	@Override
	public IConnectionParameters fromString(final String parameters)
	{
		return new DummyConnectionParameters(Integer.valueOf(parameters));
	}
}
