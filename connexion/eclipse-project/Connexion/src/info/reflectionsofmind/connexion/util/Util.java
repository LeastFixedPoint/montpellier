package info.reflectionsofmind.connexion.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;

public class Util
{
	public static <S,T> Map<T, List<S>> group(List<S> list, Function<S,T> f)
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
}
