package info.reflectionsofmind.connexion.util;

public interface IComponent
{
	void configure(Configuration configuration);
	Configuration getConfiguration();
	<T extends IComponent> T createChild(String key, Class<T> componentClass);
}
