package info.reflectionsofmind.configuration;

public class IntegerConverter implements IConverter<Integer>
{
	@Override
	public Integer convert(String value) throws InvalidValueException
	{
		try
		{
			return Integer.valueOf(value);
		}
		catch (NumberFormatException exception)
		{
			throw new InvalidValueException(value, Integer.class, exception);
		}
	}

	@Override
	public Class<Integer> getTargetClass()
	{
		return Integer.class;
	}
}
