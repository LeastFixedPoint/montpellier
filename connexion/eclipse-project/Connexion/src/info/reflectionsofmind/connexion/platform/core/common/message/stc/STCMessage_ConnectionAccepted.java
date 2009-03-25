package info.reflectionsofmind.connexion.platform.core.common.message.stc;

import static info.reflectionsofmind.connexion.platform.core.common.Participant.State.ACCEPTED;
import static info.reflectionsofmind.connexion.platform.core.common.Participant.State.CONNECTED;
import static info.reflectionsofmind.connexion.platform.core.common.Participant.State.SPECTATOR;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.server.IServer;
import info.reflectionsofmind.connexion.platform.core.server.ServerUtil;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STCMessage_ConnectionAccepted extends AbstractSTCMessage
{
	public final static String PREFIX = AbstractSTCMessage.EVENT_PREFIX + ":connection-accepted";

	private final List<String> names;
	private final List<State> states;

	public STCMessage_ConnectionAccepted(IServer server)
	{
		this( //
				ServerUtil.mapGetName(ServerUtil.getClientsByStates(server, CONNECTED, ACCEPTED, SPECTATOR)), //
				ServerUtil.mapGetState(ServerUtil.getClientsByStates(server, CONNECTED, ACCEPTED, SPECTATOR)));
	}

	private STCMessage_ConnectionAccepted(final List<String> names, final List<State> states)
	{
		this.names = names;
		this.states = states;
	}

	@Override
	public void dispatch(ISTCMessageTarget listener)
	{
		listener.onConnectionAcceptedMessage(this);
	}

	public List<String> getExistingPlayers()
	{
		return this.names;
	}

	public List<State> getStates()
	{
		return this.states;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<STCMessage_ConnectionAccepted> CODER = new AbstractCoder<STCMessage_ConnectionAccepted>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public STCMessage_ConnectionAccepted decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final List<String> names = tokens[0].isEmpty() ? new ArrayList<String>() : Util.mapDecode(Arrays.asList(tokens[0].split(",")));
			final List<State> states = tokens[1].isEmpty() ? new ArrayList<State>() : State.mapValueOf(Arrays.asList(tokens[1].split(",")));
			return new STCMessage_ConnectionAccepted(names, states);
		}

		@Override
		public String encode(STCMessage_ConnectionAccepted event)
		{
			return join(PREFIX, Util.join(Util.mapEncode(event.getExistingPlayers()), ","), Util.join(event.getStates(), ","));
		}
	};
}
