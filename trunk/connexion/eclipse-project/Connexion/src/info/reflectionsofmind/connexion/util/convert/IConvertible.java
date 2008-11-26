package info.reflectionsofmind.connexion.util.convert;

public interface IConvertible<SourceType extends IConvertible<SourceType>>
{
	IMessage<SourceType> encode();

	public interface IMessage<SourceType>
	{
		SourceType decode();
		String getString();
	}
}
