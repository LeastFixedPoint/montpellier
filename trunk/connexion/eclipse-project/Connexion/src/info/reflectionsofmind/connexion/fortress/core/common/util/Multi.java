package info.reflectionsofmind.connexion.fortress.core.common.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Multi<S, T> extends AbstractList<T>
{
	private final S source;
	private final List<T> targets = new ArrayList<T>();

	public Multi(final S source)
	{
		this.source = source;
	}
	
	public S getSource()
	{
		return this.source;
	}

	public void link(final Multi<T, S> targetLink)
	{
		this.targets.add(targetLink.source);
		targetLink.targets.add(this.source);
	}

	public void link(final Link<T, S> targetLink)
	{
		this.targets.add(targetLink.getSource());
		targetLink.setTarget(this.source);
	}

	@Override
	public T get(int index)
	{
		return this.targets.get(index);
	}

	@Override
	public int size()
	{
		return this.targets.size();
	}
}
