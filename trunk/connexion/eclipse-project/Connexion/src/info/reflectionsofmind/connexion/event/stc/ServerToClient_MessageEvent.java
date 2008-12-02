package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public class ServerToClient_MessageEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":message";
	public final static Coder CODER = new Coder();

	private final int playerIndex;
	private final String message;

	public ServerToClient_MessageEvent(final int playerIndex, final String message)
	{
		this.playerIndex = playerIndex;
		this.message = message;
	}

	public int getPlayerIndex()
	{
		return this.playerIndex;
	}

	public String getMessage()
	{
		return this.message;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public static class Coder extends AbstractCoder<ServerToClient_MessageEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_MessageEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int playerIndex = Integer.valueOf(tokens[0]);
			final String message = Util.decode(tokens[1]);
			return new ServerToClient_MessageEvent(playerIndex, message);
		}

		@Override
		public String encode(ServerToClient_MessageEvent event)
		{
			return join(PREFIX, String.valueOf(event.getPlayerIndex()), Util.encode(event.message));
		}
	}
}
