package info.reflectionsofmind.connexion.fortress.core.common.util;

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

	public Loop<E> roll(final int offset)
	{
		return new Loop<E>(this)
		{
			@Override
			public E get(int index)
			{
				return super.get(index + offset);
			}
		};
	}

	@Override
	public int size()
	{
		return list.size();
	}
}
