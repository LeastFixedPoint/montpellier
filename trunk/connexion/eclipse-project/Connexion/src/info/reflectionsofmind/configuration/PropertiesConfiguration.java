package info.reflectionsofmind.configuration;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class PropertiesConfiguration extends AbstractConfiguration
{
	private final Properties properties;

	public PropertiesConfiguration(final Properties properties)
	{
		this.properties = properties;
	}

	@Override
	public Set<String> getKeys()
	{
		final Set<String> keys = new HashSet<String>();

		for (final String key : this.properties.stringPropertyNames())
		{
			keys.add(key.split("\\.")[0]);
		}

		return keys;
	}

	@Override
	public IConfiguration getSubset(final String subsetKey)
	{
		Properties subset = new Properties();

		for (final String key : this.properties.stringPropertyNames())
		{
			if (key.startsWith(subsetKey + "."))
			{
				subset.setProperty(key.substring(subsetKey.length() + 1), this.properties.getProperty(key));
			}
		}

		return new PropertiesConfiguration(subset);
	}

	@Override
	public String getRawValue(String key)
	{
		return this.properties.getProperty(key);
	}
}
