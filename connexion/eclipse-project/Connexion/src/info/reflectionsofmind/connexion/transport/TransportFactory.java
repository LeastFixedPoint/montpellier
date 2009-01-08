package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.Component;

public final class TransportFactory extends Component implements ITransportFactory
{
	@SuppressWarnings("unchecked")
	@Override
	public ITransport createTransport()
	{
		final String className = getConfiguration().getValues().get("class");
		
		try
		{
			return createChild("properties", (Class<ITransport>)Class.forName(className));
		}
		catch (ClassNotFoundException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
