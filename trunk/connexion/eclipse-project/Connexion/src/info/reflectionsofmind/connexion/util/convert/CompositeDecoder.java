package info.reflectionsofmind.connexion.util.convert;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeDecoder<T> implements IDecoder<T>
{
	private final List<IDecoder<? extends T>> decoders = new ArrayList<IDecoder<? extends T>>();

	public final T decode(final String string)
	{
		for (final IDecoder<? extends T> decoder : this.decoders)
			if (decoder.accepts(string))
				return decoder.decode(string);

		return null;
	}

	@Override
	public final boolean accepts(final String string)
	{
		for (final IDecoder<? extends T> decoder : this.decoders)
			if (decoder.accepts(string))
				return true;

		return false;
	}

	protected final void add(final IDecoder<? extends T> decoder)
	{
		this.decoders.add(decoder);
	}
}
