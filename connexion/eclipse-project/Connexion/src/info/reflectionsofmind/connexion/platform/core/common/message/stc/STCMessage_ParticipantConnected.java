package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** STC event: another client has connected to the server. */
public class STCMessage_ParticipantConnected extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":client-connected";

	private final String clientName;

	public STCMessage_ParticipantConnected(final String clientName)
	{
		this.clientName = clientName;
	}
	
	@Override
	public void dispatch(ISTCMessageTarget listener)
	{
		listener.onParticipantConnectedMessage(this);
	}

	public String getParticipantName()
	{
		return this.clientName;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_ParticipantConnected> CODER = new AbstractCoder<STCMessage_ParticipantConnected>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_ParticipantConnected decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new STCMessage_ParticipantConnected(playerName);
		}

		@Override
		public String encode(final STCMessage_ParticipantConnected event)
		{
			return join(PREFIX, Util.encode(event.getParticipantName()));
		}
	};
}
