package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.config.ConfigurationUtil;
import info.reflectionsofmind.connexion.util.Component;

public class TransportUtil
{
	@SuppressWarnings("unchecked")
	public static ITransport createTransport(Component parent, String transportKey)
	{
		try
		{
			final Class<ITransport> tranportClass = (Class<ITransport>) Class.forName( //
					ConfigurationUtil.getValue(parent.getConfiguration(), // 
							"transports." + transportKey + ".class"));

			return parent.createChild("transports." + transportKey + ".properties", tranportClass);
		}
		catch (ClassNotFoundException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
