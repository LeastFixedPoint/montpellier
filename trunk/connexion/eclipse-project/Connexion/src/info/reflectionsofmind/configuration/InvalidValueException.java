package info.reflectionsofmind.configuration;

public class InvalidValueException extends ConversionException
{
	public InvalidValueException(String string, Class<?> clazz, Exception exception)
	{
		super("Error converting [" + string + "] to [" + clazz.getName() + "].", exception);
	}
}
