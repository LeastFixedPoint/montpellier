package info.reflectionsofmind.configuration;

public class StringConverter implements IConverter<String>
{
	@Override
	public String convert(String value) throws InvalidValueException
	{
		return value;
	}

	@Override
	public Class<String> getTargetClass()
	{
		return String.class;
	}
}
