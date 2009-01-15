package info.reflectionsofmind.configuration;

public class ConverterNotFoundException extends ConversionException
{
	public ConverterNotFoundException(String string, Class<?> clazz)
	{
		super("Converter not found for value [" + string + "] of class [" + clazz.getName() + "].");
	}
}
