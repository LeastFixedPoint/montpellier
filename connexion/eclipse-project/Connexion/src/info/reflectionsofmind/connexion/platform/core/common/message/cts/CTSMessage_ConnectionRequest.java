package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class CTSMessage_ConnectionRequest extends AbstractCTSMessage
{
	private final static String PREFIX = AbstractCTSMessage.EVENT_PREFIX + ":connection-request";

	private final String playerName;

	public CTSMessage_ConnectionRequest(final String playerName)
	{
		this.playerName = playerName;
	}
	
	@Override
	public void dispatch(IClientNode from, ICTSMessageTarget target)
	{
		target.onConnectionRequest(from, this);
	}

	public String getPlayerName()
	{
		return this.playerName;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<CTSMessage_ConnectionRequest> CODER = // 
	new AbstractCoder<CTSMessage_ConnectionRequest>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public CTSMessage_ConnectionRequest decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new CTSMessage_ConnectionRequest(playerName);
		}

		@Override
		public String encode(final CTSMessage_ConnectionRequest event)
		{
			return join(PREFIX, Util.encode(event.getPlayerName()));
		}
	};
}
