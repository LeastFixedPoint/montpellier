package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Server-to-client event: a client has (been) disconnected from game. */
public class STCMessage_ParticipantDisconnected extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":client-disconnected";

	private final int clientIndex;
	private final DisconnectReason reason;

	public STCMessage_ParticipantDisconnected(final int clientIndex, final DisconnectReason reason)
	{
		this.clientIndex = clientIndex;
		this.reason = reason;
	}

	@Override
	public void dispatch(final ISTCMessageTarget listener)
	{
		listener.onParticipantDisconnectedMessage(this);
	}

	public int getParticipantIndex()
	{
		return this.clientIndex;
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

	public final static ICoder<STCMessage_ParticipantDisconnected> CODER = new AbstractCoder<STCMessage_ParticipantDisconnected>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_ParticipantDisconnected decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int clientIndex = Integer.valueOf(tokens[0]);
			final DisconnectReason reason = DisconnectReason.valueOf(tokens[1]);
			return new STCMessage_ParticipantDisconnected(clientIndex, reason);
		}

		@Override
		public String encode(final STCMessage_ParticipantDisconnected event)
		{
			return join(PREFIX, String.valueOf(event.getParticipantIndex()), event.reason.toString());
		}
	};
}
