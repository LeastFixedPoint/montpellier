package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class STCMessage_GameChanged extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":game-changed";

	private final String encodedGameConfig;

	public STCMessage_GameChanged(String encodedGameConfig)
	{
		this.encodedGameConfig = encodedGameConfig;
	}

	@Override
	public void dispatch(final ISTCMessageTarget listener)
	{
		listener.onGameChangedMessage(this);
	}

	public String getEncodedGameConfig()
	{
		return this.encodedGameConfig;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_GameChanged> CODER = new AbstractCoder<STCMessage_GameChanged>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_GameChanged decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new STCMessage_GameChanged(tokens[0]);
		}

		@Override
		public String encode(final STCMessage_GameChanged event)
		{
			return PREFIX + ":" + event.getEncodedGameConfig();
		}
	};
}
