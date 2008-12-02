package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

/** STC event: another player has connected to the game. */
public class ServerToClient_PlayerConnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-connected";
	public final static Coder CODER = new Coder();

	private final String playerName;

	public ServerToClient_PlayerConnectedEvent(final String playerName)
	{
		this.playerName = playerName;
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

	public static class Coder extends AbstractCoder<ServerToClient_PlayerConnectedEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerConnectedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new ServerToClient_PlayerConnectedEvent(playerName);
		}

		@Override
		public String encode(ServerToClient_PlayerConnectedEvent event)
		{
			return join(PREFIX, Util.encode(event.getPlayerName()));
		}
	}
}
