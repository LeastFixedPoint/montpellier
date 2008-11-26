package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

import org.jivesoftware.smack.util.Base64;

public class ServerToClient_GameStartedEvent extends ServerToClientEvent<ServerToClient_GameStartedEvent>
{
	private final String gameName;
	private final String initialTileCode;
	private final String currentTileCode;
	private final Integer totalTiles;

	public ServerToClient_GameStartedEvent( //
			final String gameName, // 
			final String initialTileCode, //
			final String currentTileCode, final Integer totalTiles)
	{
		super();
		this.gameName = gameName;
		this.initialTileCode = initialTileCode;
		this.currentTileCode = currentTileCode;
		this.totalTiles = totalTiles;
	}

	public String getGameName()
	{
		return this.gameName;
	}

	public String getInitialTileCode()
	{
		return this.initialTileCode;
	}

	public String getCurrentTileCode()
	{
		return this.currentTileCode;
	}

	public Integer getTotalNumberOfTiles()
	{
		return this.totalTiles;
	}

	@Override
	public IMessage<ServerToClient_GameStartedEvent> encode()
	{
		final String[] tokens = new String[] { Base64.encodeBytes(getGameName().getBytes()), //
				getInitialTileCode(), // 
				getCurrentTileCode(), //
				String.valueOf(getTotalNumberOfTiles()) };

		return new AbstractMessage<ServerToClient_GameStartedEvent>(PREFIX + ":game-started", tokens)
		{
			@Override
			public ServerToClient_GameStartedEvent decode()
			{
				final String[] tokens = getTokens();

				return new ServerToClient_GameStartedEvent( //
						new String(Base64.decode(tokens[0])), //
						tokens[1], tokens[2], Integer.parseInt(tokens[3]));
			}
		};
	}
}
