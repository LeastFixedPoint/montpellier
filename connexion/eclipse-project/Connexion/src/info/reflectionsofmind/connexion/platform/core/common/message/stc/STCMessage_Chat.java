package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class STCMessage_Chat extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":message";

	private final Integer clientIndex;
	private final String message;

	public STCMessage_Chat(final Integer clientIndex, final String message)
	{
		this.clientIndex = clientIndex;
		this.message = message;
	}
	
	@Override
	public void dispatch(ISTCMessageTarget listener)
	{
		listener.onChatMessage(this);
	}

	public Integer getParticipantIndex()
	{
		return this.clientIndex;
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

	public final static ICoder<STCMessage_Chat> CODER = new AbstractCoder<STCMessage_Chat>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_Chat decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Integer clientIndex = tokens[0].isEmpty() ? null : Integer.valueOf(tokens[0]);
			final String message = Util.decode(tokens[1]);
			return new STCMessage_Chat(clientIndex, message);
		}

		@Override
		public String encode(final STCMessage_Chat event)
		{
			final String clientIndex = event.getParticipantIndex() == null ? "" : event.getParticipantIndex().toString();
			return join(PREFIX, clientIndex, Util.encode(event.message));
		}
	};
}
