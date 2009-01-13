package info.reflectionsofmind.connexion.transport;

public final class TransportFactory implements ITransportFactory
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
