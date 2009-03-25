package info.reflectionsofmind.connexion.util.convert;

import info.reflectionsofmind.connexion.util.Util;

import java.util.Arrays;

public abstract class AbstractCoder<T> implements ICoder<T>
{
	@Override
	public boolean accepts(String string)
	{
		return true;
	}

	protected String join(String... strings)
	{
		return Util.join(Arrays.asList(strings), ":");
	}

	protected String[] split(String prefix, String string)
	{
		final String[] tokens = string.split(":", Integer.MIN_VALUE);
		return Arrays.copyOfRange(tokens, prefix.split(":").length, tokens.length);
	}
}
