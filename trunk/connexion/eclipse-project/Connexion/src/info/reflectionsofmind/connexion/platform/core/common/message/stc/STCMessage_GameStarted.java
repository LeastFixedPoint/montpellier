package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class STCMessage_GameStarted extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":game-started";

	private final String encodedStartInfo;

	public STCMessage_GameStarted(String encodedStartInfo)
	{
		this.encodedStartInfo= encodedStartInfo;
	}

	@Override
	public void dispatch(final ISTCMessageTarget listener)
	{
		listener.onGameStartedMessage(this);
	}

	public String getEncodedStartInfo()
	{
		return this.encodedStartInfo;
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
			return new STCMessage_GameStarted(tokens[1]);
		}

		@Override
		public String encode(final STCMessage_GameStarted message)
		{
			return PREFIX + ":" + message.getEncodedStartInfo();
		}
	};
}
