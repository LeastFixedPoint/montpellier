package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

public class ServerToClient_MessageEvent extends ServerToClientEvent<ServerToClient_MessageEvent>
{
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
	public IMessage<ServerToClient_MessageEvent> encode()
	{
		return new AbstractMessage<ServerToClient_MessageEvent>( //
				PREFIX + ":connection-request", String.valueOf(this.playerIndex), Util.encode(this.message))
		{
			@Override
			public ServerToClient_MessageEvent decode()
			{
				return new ServerToClient_MessageEvent(Integer.parseInt(getTokens()[0]), getTokens()[1]);
			}
		};
	}
}
