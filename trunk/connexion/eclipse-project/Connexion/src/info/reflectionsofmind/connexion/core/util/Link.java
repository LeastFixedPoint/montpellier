package info.reflectionsofmind.connexion.core.util;

public class Link<S, T>
{
	private final S source;
	private T target;

	public Link(final S source)
	{
		this.source = source;
	}

	public void link(final Link<T, S> targetLink)
	{
		this.target = targetLink.getSource();
		targetLink.target = getSource();
	}
	
	public void link(final Multi<T, S> targetLink)
	{
		targetLink.link(this);
	}

	public S getSource()
	{
		return this.source;
	}

	public void setTarget(final T target)
	{
		if (this.target != null) throw new RuntimeException("Target is already set.");
		this.target = target;
	}

	public T get()
	{
		if (this.target == null) throw new RuntimeException("Target is not set.");
		return this.target;
	}

	@Override
	public boolean equals(final Object source)
	{
		return this.source.equals(source);
	}
	
	
}
