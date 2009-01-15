package info.reflectionsofmind.configuration;

import java.util.Set;

public interface IConfiguration
{
	IConfiguration getSubset(String key);
	Set<String> getKeys();
	<T> T getValue(String key, Class<T> convertToClass) throws ConversionException;
	String getRawValue(String key);
}
