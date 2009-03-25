package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.server.IRemoteClient;
import info.reflectionsofmind.connexion.platform.core.server.IServer;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class STCMessage_ParticipantStateChanged extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":client-state-changed";

	private final int clientIndex;
	private final State newState;

	public STCMessage_ParticipantStateChanged(IServer server, IRemoteClient client)
	{
		this(server.getClients().indexOf(client), client.getParticipant().getState());
	}

	public STCMessage_ParticipantStateChanged(int clientIndex, State newState)
	{
		this.clientIndex = clientIndex;
		this.newState = newState;
	}

	@Override
	public void dispatch(ISTCMessageTarget listener)
	{
		listener.onParticipantStateChangedMessage(this);
	}

	public int getParticipantIndex()
	{
		return this.clientIndex;
	}
	
	public State getNewState()
	{
		return this.newState;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_ParticipantStateChanged> CODER = new AbstractCoder<STCMessage_ParticipantStateChanged>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_ParticipantStateChanged decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new STCMessage_ParticipantStateChanged(Integer.parseInt(tokens[0]), State.valueOf(tokens[1]));
		}

		@Override
		public String encode(STCMessage_ParticipantStateChanged event)
		{
			return join(PREFIX, String.valueOf(event.getParticipantIndex()), event.getNewState().toString());
		}
	};
}
