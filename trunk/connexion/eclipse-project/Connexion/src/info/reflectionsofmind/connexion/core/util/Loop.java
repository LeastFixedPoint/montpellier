package info.reflectionsofmind.connexion.core.util;

import java.util.AbstractList;
import java.util.List;

public class Loop<E> extends AbstractList<E>
{
	private final List<E> list;
	
	public Loop(List<E> list)
	{
		this.list = list;
	}

	@Override
	public E get(int index)
	{
		final int actualIndex;
		
		if (index < 0)
		{
			actualIndex = list.size() + (index % list.size());
		}
		else
		{
			actualIndex = index % list.size();
		}
		
		return list.get(actualIndex);
	}

	@Override
	public int size()
	{
		return list.size();
	}
}
