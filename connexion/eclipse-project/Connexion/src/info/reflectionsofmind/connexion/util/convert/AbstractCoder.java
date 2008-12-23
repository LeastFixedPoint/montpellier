package info.reflectionsofmind.connexion.util.convert;

import java.util.Arrays;

import info.reflectionsofmind.connexion.util.Util;

public abstract class AbstractCoder<T> implements ICoder<T>
{
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
