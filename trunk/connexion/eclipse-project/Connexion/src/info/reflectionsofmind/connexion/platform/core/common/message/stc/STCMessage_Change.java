package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** A turn message coming from server. */
public class STCMessage_Change extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":turn";

	private final String encodedChange;

	public STCMessage_Change(String encodedChange)
	{
		this.encodedChange = encodedChange;
	}

	@Override
	public void dispatch(ISTCMessageTarget listener)
	{
		listener.onChangeMessage(this);
	}

	public String getEncodedChange()
	{
		return this.encodedChange;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_Change> CODER = new AbstractCoder<STCMessage_Change>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_Change decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new STCMessage_Change(tokens[0]);
		}

		@Override
		public String encode(final STCMessage_Change message)
		{
			return join(PREFIX, message.getEncodedChange());
		}
	};
}
