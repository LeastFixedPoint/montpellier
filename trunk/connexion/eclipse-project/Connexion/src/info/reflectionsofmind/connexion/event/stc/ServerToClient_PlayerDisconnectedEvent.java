package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

/** Server-to-client event: a player has (been) disconnected from game. */
public class ServerToClient_PlayerDisconnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-disconnected";
	public final static Coder CODER = new Coder();

	private final int playerIndex;
	private final ServerSideDisconnectReason reason;

	public ServerToClient_PlayerDisconnectedEvent(final int playerIndex, final ServerSideDisconnectReason reason)
	{
		this.playerIndex = playerIndex;
		this.reason = reason;
	}

	public int getPlayerIndex()
	{
		return this.playerIndex;
	}

	public ServerSideDisconnectReason getReason()
	{
		return this.reason;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public static class Coder extends AbstractCoder<ServerToClient_PlayerDisconnectedEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerDisconnectedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int playerIndex = Integer.valueOf(tokens[0]);
			final ServerSideDisconnectReason reason = ServerSideDisconnectReason.valueOf(tokens[1]);
			return new ServerToClient_PlayerDisconnectedEvent(playerIndex, reason);
		}

		@Override
		public String encode(ServerToClient_PlayerDisconnectedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getPlayerIndex()), event.reason.toString());
		}
	}

}
