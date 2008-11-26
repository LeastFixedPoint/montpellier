package info.reflectionsofmind.connexion.util.convert;

public interface CopyOfIMessage<SourceType extends IConvertible<SourceType>>
{
	SourceType decode();
	String getString();
}
