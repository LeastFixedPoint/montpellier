package info.reflectionsofmind.connexion.util.convert;

public interface ICoder<T>
{
	boolean accepts(String string);
	T decode(String string) throws DecodingException;
	String encode(T t) throws EncodingException;
}
