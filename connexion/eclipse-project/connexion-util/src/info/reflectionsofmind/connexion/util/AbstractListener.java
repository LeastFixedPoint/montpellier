package info.reflectionsofmind.connexion.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class AbstractListener<ListenerType>
{
	private final List<ListenerType> listeners = new ArrayList<ListenerType>();

	public final void addListener(final ListenerType listener)
	{
		this.listeners.add(listener);
	}

	public final void removeListener(final ListenerType listener)
	{
		this.listeners.remove(listener);
	}

	protected final List<ListenerType> getListeners()
	{
		return ImmutableList.copyOf(this.listeners);
	}
}
