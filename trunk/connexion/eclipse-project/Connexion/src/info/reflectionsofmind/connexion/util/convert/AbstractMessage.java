package info.reflectionsofmind.connexion.util.convert;

import java.util.Arrays;

import info.reflectionsofmind.connexion.util.Util;

public abstract class AbstractMessage<ConvertibleType extends IConvertible<ConvertibleType>> implements IConvertible.IMessage<ConvertibleType>
{
	public final static String SEPARATOR = ":";

	private final String prefix;
	private final String[] tokens;

	public AbstractMessage(final String prefix, final String... tokens)
	{
		if (tokens.length == 0)
		{
			this.prefix = null;
			this.tokens = new String[] { prefix };
		}
		else
		{
			this.prefix = prefix;
			this.tokens = tokens;
		}
	}

	@Override
	public String getString()
	{
		if (this.prefix != null)
		{
			return "[" + this.prefix + SEPARATOR + Util.join(Arrays.asList(this.tokens), SEPARATOR) + "]";
		}
		else
		{
			return "[" + Util.join(Arrays.asList(this.tokens), SEPARATOR) + "]";
		}
	}

	public String[] getTokens()
	{
		return this.tokens;
	}
}
