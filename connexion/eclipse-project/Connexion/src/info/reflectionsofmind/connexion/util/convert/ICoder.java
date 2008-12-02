package info.reflectionsofmind.connexion.util.convert;

public interface ICoder<T>
{
	boolean accepts(String string);
	T decode(String string);
	String encode(T t);
}
