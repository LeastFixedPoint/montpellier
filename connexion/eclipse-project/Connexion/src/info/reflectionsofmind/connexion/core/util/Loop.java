package info.reflectionsofmind.connexion.core.util;

import java.util.List;

public class Loop<E>
{
	private final List<E> list;
	private int index = 0;
	
	public Loop(List<E> list)
	{
		this.list = list;
	}

	public void advance()
	{
		index = (index >= list.size()) ? 0 : (index + 1);
	}

	public E current()
	{
		return list.get(index);
	}
}
