package info.reflectionsofmind.connexion.util.convert;

public interface IDecoder<T>
{
	boolean accepts(String string);
	T decode(String string);
}
