package info.reflectionsofmind.connexion.common.event.stc;

import static info.reflectionsofmind.connexion.common.Participant.State.ACCEPTED;
import static info.reflectionsofmind.connexion.common.Participant.State.CONNECTED;
import static info.reflectionsofmind.connexion.common.Participant.State.SPECTATOR;
import info.reflectionsofmind.connexion.common.Participant.State;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.server.ServerUtil;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerToClient_ConnectionAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":connection-accepted";

	private final List<String> names;
	private final List<State> states;

	public ServerToClient_ConnectionAcceptedEvent(IServer server)
	{
		this( //
				ServerUtil.mapGetName(ServerUtil.getClientsByStates(server, CONNECTED, ACCEPTED, SPECTATOR)), //
				ServerUtil.mapGetState(ServerUtil.getClientsByStates(server, CONNECTED, ACCEPTED, SPECTATOR)));
	}

	private ServerToClient_ConnectionAcceptedEvent(final List<String> names, final List<State> states)
	{
		this.names = names;
		this.states = states;
	}

	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onConnectionAccepted(this);
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

	public final static ICoder<ServerToClient_ConnectionAcceptedEvent> CODER = new AbstractCoder<ServerToClient_ConnectionAcceptedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_ConnectionAcceptedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final List<String> names = tokens[0].isEmpty() ? new ArrayList<String>() : Util.mapDecode(Arrays.asList(tokens[0].split(",")));
			final List<State> states = tokens[1].isEmpty() ? new ArrayList<State>() : State.mapValueOf(Arrays.asList(tokens[1].split(",")));
			return new ServerToClient_ConnectionAcceptedEvent(names, states);
		}

		@Override
		public String encode(ServerToClient_ConnectionAcceptedEvent event)
		{
			return join(PREFIX, Util.join(Util.mapEncode(event.getExistingPlayers()), ","), Util.join(event.getStates(), ","));
		}
	};
}
