package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

/** Server-to-client event: client has disconnected by its own initiative. */
public class ServerToClient_PlayerDisconnectedEvent extends ServerToClientEvent<ServerToClient_PlayerDisconnectedEvent>
{
	public enum Reason
	{
		SERVER_REQUEST, CLIENT_REQUEST, CONNECTION_FAILURE
	}

	private final int playerIndex;
	private final Reason reason;

	public ServerToClient_PlayerDisconnectedEvent(final int playerIndex, final Reason reason)
	{
		this.playerIndex = playerIndex;
		this.reason = reason;
	}

	public int getPlayerIndex()
	{
		return this.playerIndex;
	}

	public Reason getReason()
	{
		return this.reason;
	}

	@Override
	public IMessage<ServerToClient_PlayerDisconnectedEvent> encode()
	{
		final String[] tokens = new String[] { String.valueOf(playerIndex), reason.toString() };

		return new AbstractMessage<ServerToClient_PlayerDisconnectedEvent>(PREFIX + ":player-disconnected", tokens)
		{
			@Override
			public ServerToClient_PlayerDisconnectedEvent decode()
			{
				final String[] tokens = getTokens();
				return new ServerToClient_PlayerDisconnectedEvent( //
						Integer.parseInt(tokens[0]), Reason.valueOf(tokens[1]));
			}
		};
	}
}
