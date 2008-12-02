package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

/** A turn event coming from server. */
public class ServerToClient_TurnEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":turn";
	public final static Coder CODER = new Coder();

	private final Turn turn;
	private final String currentTileCode;

	public ServerToClient_TurnEvent(final Turn turn, final String currentTileCode)
	{
		this.turn = turn;
		this.currentTileCode = currentTileCode;
	}

	public Turn getTurn()
	{
		return this.turn;
	}

	public String getCurrentTileCode()
	{
		return this.currentTileCode;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public static class Coder extends AbstractCoder<ServerToClient_TurnEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_TurnEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Turn turn = Turn.CODER.decode(Util.decode(tokens[0]));
			final String currentTileCode = tokens[1];
			return new ServerToClient_TurnEvent(turn, currentTileCode);
		}

		@Override
		public String encode(ServerToClient_TurnEvent event)
		{
			return join(PREFIX, Util.encode(Turn.CODER.encode(event.getTurn())), event.getCurrentTileCode());
		}
	}
}
