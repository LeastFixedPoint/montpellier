package info.reflectionsofmind.connexion.config;

import info.reflectionsofmind.connexion.util.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public final class ConfigurationUtil
{
	private ConfigurationUtil()
	{
		throw new UnsupportedOperationException("Utility class");
	}

	@SuppressWarnings("unchecked")
	public static Configuration loadFromFile(String fileName)
	{
		final Properties properties = new Properties();

		try
		{
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}

		return new Configuration((Map) properties);
	}

	public static String getValue(Configuration configuration, String path)
	{
		if (path.contains("."))
		{
			final Configuration category = getCategory(configuration, path.substring(0, path.lastIndexOf('.')));
			final String[] tokens = path.split("\\.");
			return category.getValues().get(tokens[tokens.length - 1]);
		}
		else
		{
			return configuration.getValues().get(path);
		}
	}

	public static Configuration getCategory(Configuration configuration, String path)
	{
		if (path.contains("."))
		{
			final String[] tokens = path.split("\\.");

			if (configuration.getCategories().containsKey(tokens[0]))
			{
				return getCategory( //
						configuration.getCategories().get(tokens[0]), // 
						path.substring(path.indexOf('.') + 1));
			}
			else
			{
				return null;
			}
		}
		else
		{
			return configuration.getCategories().get(path);
		}
	}
}
