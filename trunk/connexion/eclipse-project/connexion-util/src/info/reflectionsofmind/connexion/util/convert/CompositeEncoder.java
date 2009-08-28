package info.reflectionsofmind.connexion.util.convert;

import java.util.HashMap;
import java.util.Map;

public abstract class CompositeEncoder<T> implements IEncoder<T>
{
	private final Map<Class<? extends T>, IEncoder<? extends T>> encoders = new HashMap<Class<? extends T>, IEncoder<? extends T>>();

	@SuppressWarnings("unchecked")
	public final String encode(T object)
	{
		for (Class<? extends T> clazz : this.encoders.keySet())
			if (object.getClass() == clazz)
				return ((IEncoder<T>) encoders.get(clazz)).encode(object);

		return null;
	}

	protected final void add(Class<? extends T> clazz, IEncoder<? extends T> encoder)
	{
		this.encoders.put(clazz, encoder);
	}
}
