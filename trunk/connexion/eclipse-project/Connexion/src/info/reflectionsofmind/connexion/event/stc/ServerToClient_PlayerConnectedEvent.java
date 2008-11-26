package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

/** STC event: another player has connected to the game. */
public class ServerToClient_PlayerConnectedEvent extends ServerToClientEvent<ServerToClient_PlayerConnectedEvent>
{
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
	public IMessage<ServerToClient_PlayerConnectedEvent> encode()
	{
		return new AbstractMessage<ServerToClient_PlayerConnectedEvent>(PREFIX + ":player-connected", getPlayerName())
		{
			@Override
			public ServerToClient_PlayerConnectedEvent decode()
			{
				return new ServerToClient_PlayerConnectedEvent(getTokens()[0]);
			}
		};
	}
}
