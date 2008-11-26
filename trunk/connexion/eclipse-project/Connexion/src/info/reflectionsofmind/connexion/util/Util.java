package info.reflectionsofmind.connexion.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.util.Base64;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Util
{
	public static <S, T> Map<T, List<S>> group(List<S> list, Function<S, T> f)
	{
		final Map<T, List<S>> map = new HashMap<T, List<S>>();

		for (S s : list)
		{
			T t = f.apply(s);

			if (!map.containsKey(t)) map.put(t, new ArrayList<S>());
			map.get(t).add(s);
		}

		return map;
	}

	public static <S, T> List<T> map(Map<S, T> map, List<S> source)
	{
		final List<T> result = new ArrayList<T>();

		for (S s : source)
			result.add(map.get(s));

		return result;
	}

	public static String join(List<String> existingPlayers, String separator)
	{
		if (existingPlayers.size() == 0) return "";
		if (existingPlayers.size() == 1) return existingPlayers.get(0);

		final StringBuilder builder = new StringBuilder(existingPlayers.get(0));

		for (int i = 1; i < existingPlayers.size(); i++)
		{
			builder.append(separator).append(existingPlayers.get(i));
		}

		return builder.toString();
	}

	public static String encode(String string)
	{
		return Base64.encodeBytes(string.getBytes());
	}

	public static List<String> mapEncode(List<String> strings)
	{
		return Lists.transform(strings, new Function<String, String>()
		{
			@Override
			public String apply(String string)
			{
				return encode(string);
			}
		});
	}

	public static String decode(String string)
	{
		return new String(Base64.decode(string));
	}

	public static List<String> mapDecode(List<String> strings)
	{
		return Lists.transform(strings, new Function<String, String>()
		{
			@Override
			public String apply(String string)
			{
				return decode(string);
			}
		});
	}
}
