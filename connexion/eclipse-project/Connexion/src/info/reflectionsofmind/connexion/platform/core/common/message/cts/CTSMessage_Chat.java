package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class CTSMessage_Chat extends AbstractCTSMessage
{
	public final static String PREFIX = AbstractCTSMessage.EVENT_PREFIX + ":message";

	private final String message;

	public CTSMessage_Chat(final String message)
	{
		this.message = message;
	}
	
	@Override
	public void dispatch(IClientNode from, ICTSMessageTarget target)
	{
		target.onChatMessage(from, this);
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<CTSMessage_Chat> CODER = new AbstractCoder<CTSMessage_Chat>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public CTSMessage_Chat decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String message = Util.decode(tokens[0]);
			return new CTSMessage_Chat(message);
		}

		@Override
		public String encode(CTSMessage_Chat event)
		{
			return join(PREFIX, Util.encode(event.getMessage()));
		}
	};
}
