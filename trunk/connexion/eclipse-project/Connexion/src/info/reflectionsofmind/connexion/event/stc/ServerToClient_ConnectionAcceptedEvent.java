package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

import java.util.Arrays;
import java.util.List;

public class ServerToClient_ConnectionAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":connection-accepted";
	public final static Coder CODER = new Coder();
	
	private final List<String> existingPlayers;

	public ServerToClient_ConnectionAcceptedEvent(final List<String> existingPlayers)
	{
		this.existingPlayers = existingPlayers;
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
	
	public static class Coder extends AbstractCoder<ServerToClient_ConnectionAcceptedEvent>
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
	}
}

