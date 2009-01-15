package info.reflectionsofmind.configuration;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class AbstractConfiguration implements IConfiguration
{
	private final List<IConverter<?>> converters = ImmutableList.<IConverter<?>> of(new StringConverter(), new IntegerConverter());

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T getValue(String key, Class<T> convertToClass) throws ConversionException
	{
		for (IConverter<?> converter : this.converters)
		{
			if (converter.getTargetClass() == convertToClass) return (T) converter.convert(getRawValue(key));

		}

		throw new ConverterNotFoundException(key, convertToClass);
	}
}
