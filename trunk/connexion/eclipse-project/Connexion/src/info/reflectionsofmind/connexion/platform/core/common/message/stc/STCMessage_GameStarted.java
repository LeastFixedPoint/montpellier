package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class STCMessage_GameStarted extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":game-started";

	private final String encodedInitInfo;

	public STCMessage_GameStarted(String encodedInitInfo)
	{
		this.encodedInitInfo = encodedInitInfo;
	}

	@Override
	public void dispatch(final ISTCMessageTarget listener)
	{
		listener.onGameStartedMessage(this);
	}

	public String getEncodedInitInfo()
	{
		return this.encodedInitInfo;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_GameStarted> CODER = new AbstractCoder<STCMessage_GameStarted>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_GameStarted decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new STCMessage_GameStarted(tokens[0]);
		}

		@Override
		public String encode(final STCMessage_GameStarted event)
		{
			return PREFIX + ":" + event.getEncodedInitInfo();
		}
	};
}
