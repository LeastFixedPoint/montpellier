package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

/** A turn event coming from server. */
public class ServerToClient_TurnEvent extends ServerToClientEvent<ServerToClient_TurnEvent>
{
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
	public IMessage<ServerToClient_TurnEvent> encode()
	{
		final String[] tokens = new String[] { this.turn.encode().getString(), this.currentTileCode };

		return new AbstractMessage<ServerToClient_TurnEvent>(PREFIX + ":turn", tokens)
		{
			@Override
			public ServerToClient_TurnEvent decode()
			{
				final String[] tokens = getTokens();
				return new ServerToClient_TurnEvent(new Turn.Message(tokens[3]).decode(), tokens[4]);
			}
		};
	}
}
