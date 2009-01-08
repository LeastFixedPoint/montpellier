package info.reflectionsofmind.connexion.util;

import info.reflectionsofmind.connexion.config.ConfigurationUtil;

public abstract class Component implements IComponent
{
	private Configuration configuration;

	public final void configure(final Configuration configuration)
	{
		this.configuration = configuration;
		afterConfigure();
	}
	
	protected void afterConfigure()
	{
		// To be overridden
	}

	public final Configuration getConfiguration()
	{
		return this.configuration;
	}

	public final <T extends IComponent> T createChild(final String key, final Class<T> componentClass)
	{
		try 
		{
			final T component = componentClass.newInstance();
			component.configure(ConfigurationUtil.getCategory(this.configuration, key));
			return component;
		}
		catch (final Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
