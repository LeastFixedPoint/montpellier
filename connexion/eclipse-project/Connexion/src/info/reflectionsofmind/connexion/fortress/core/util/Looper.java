package info.reflectionsofmind.connexion.fortress.core.util;

import java.util.Iterator;

public final class Looper<T>
{
	private final Iterable<T> iterable;
	private Iterator<T> iterator;

	public Looper(final Iterable<T> iterable)
	{
		this.iterable = iterable;
		reset();
	}

	public T next()
	{
		if (!this.iterator.hasNext()) reset();
		return this.iterator.next();
	}

	public void reset()
	{
		this.iterator = this.iterable.iterator();
	}
}
