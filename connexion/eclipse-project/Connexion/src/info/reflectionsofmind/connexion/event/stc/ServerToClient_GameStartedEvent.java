package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public class ServerToClient_GameStartedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":game-started";
	public final static Coder CODER = new Coder();

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
	public String encode()
	{
		return CODER.encode(this);
	}

	public static class Coder extends AbstractCoder<ServerToClient_GameStartedEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_GameStartedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);

			return new ServerToClient_GameStartedEvent( //
					new String(Util.decode(tokens[0])), //
					tokens[1], tokens[2], Integer.parseInt(tokens[3]));
		}

		@Override
		public String encode(ServerToClient_GameStartedEvent event)
		{
			return PREFIX // 
					+ ":" + Util.encode(event.getGameName()) // 
					+ ":" + event.getInitialTileCode()//
					+ ":" + event.getCurrentTileCode()//
					+ ":" + String.valueOf(event.getTotalNumberOfTiles());
		}
	}
}
