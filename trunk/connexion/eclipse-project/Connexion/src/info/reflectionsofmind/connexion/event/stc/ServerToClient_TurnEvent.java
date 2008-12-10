package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** A turn event coming from server. */
public class ServerToClient_TurnEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":turn";

	private final Turn turn;
	private final String currentTileCode;

	public ServerToClient_TurnEvent(final Turn turn, final String currentTileCode)
	{
		this.turn = turn;
		this.currentTileCode = currentTileCode;
	}
	
	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onTurn(this);
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

	public final static ICoder<ServerToClient_TurnEvent> CODER = new AbstractCoder<ServerToClient_TurnEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_TurnEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Turn turn = Turn.CODER.decode(Util.decode(tokens[0]));
			final String currentTileCode = tokens[1];
			return new ServerToClient_TurnEvent(turn, currentTileCode);
		}

		@Override
		public String encode(final ServerToClient_TurnEvent event)
		{
			return join(PREFIX, Util.encode(Turn.CODER.encode(event.getTurn())), event.getCurrentTileCode());
		}
	};
}
