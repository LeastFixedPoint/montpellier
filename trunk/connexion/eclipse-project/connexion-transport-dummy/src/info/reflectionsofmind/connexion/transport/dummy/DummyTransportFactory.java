package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.ITransportFactory;

public final class DummyTransportFactory implements ITransportFactory
{
	public DummyTransport createTransport(final String string)
	{
		return new DummyTransport(string);
	}
	
	@Override
	public String getName()
	{
		return "Dummy";
	}
}
