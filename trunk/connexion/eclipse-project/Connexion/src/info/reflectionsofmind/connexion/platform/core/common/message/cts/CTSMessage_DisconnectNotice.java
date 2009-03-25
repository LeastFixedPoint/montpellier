package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Event: client has disconnected by its own initiative. */
public class CTSMessage_DisconnectNotice extends AbstractCTSMessage
{
	private final static String PREFIX = AbstractCTSMessage.EVENT_PREFIX + ":disconnect";

	private final DisconnectReason reason;

	public CTSMessage_DisconnectNotice(final DisconnectReason reason)
	{
		this.reason = reason;
	}
	
	@Override
	public void dispatch(IClientNode from, ICTSMessageTarget target)
	{
		target.onDisconnectNotice(from, this);
	}

	public DisconnectReason getReason()
	{
		return this.reason;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<CTSMessage_DisconnectNotice> CODER = new AbstractCoder<CTSMessage_DisconnectNotice>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public CTSMessage_DisconnectNotice decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final DisconnectReason reason = DisconnectReason.valueOf(tokens[0]);
			return new CTSMessage_DisconnectNotice(reason);
		}

		@Override
		public String encode(final CTSMessage_DisconnectNotice event)
		{
			return join(PREFIX, event.getReason().toString());
		}
	};
}
