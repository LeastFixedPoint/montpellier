package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.GameUtil;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.Arrays;
import java.util.List;

public class ServerToClient_ConnectionAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":connection-accepted";
	
	private final List<String> existingPlayers;
	
	public ServerToClient_ConnectionAcceptedEvent(IServer server)
	{
		this(GameUtil.getNames(ServerUtil.getPlayers(server)));
	}

	private ServerToClient_ConnectionAcceptedEvent(final List<String> existingPlayers)
	{
		this.existingPlayers = existingPlayers;
	}

	@Override
	public void dispatch(IListener listener)
	{
		listener.onConnectionAccepted(this);
	}

	public List<String> getExistingPlayers()
	{
		return this.existingPlayers;
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
			final List<String> strings = Util.mapDecode(Arrays.asList(tokens[0].split(",")));
			return new ServerToClient_ConnectionAcceptedEvent(strings);
		}

		@Override
		public String encode(ServerToClient_ConnectionAcceptedEvent event)
		{
			return join(PREFIX, Util.join(Util.mapEncode(event.getExistingPlayers()), ","));
		}
	};
}

