package info.reflectionsofmind.configuration;

public interface IConverter<T>
{
	T convert(String value) throws InvalidValueException;
	Class<T> getTargetClass();
}
