package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.transport.ITransport;

public class ClientUtil
{
	@SuppressWarnings("unchecked")
	public static <T> T findTransport(final IClient client, final Class<T> transportClass)
	{
		for (final ITransport transport : client.getTransports())
		{
			if (transportClass.isInstance(transport))
			{
				return (T) transport;
			}
		}
	
		return null;
	}
}
