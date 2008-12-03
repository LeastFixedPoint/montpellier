package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.GameUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_GameStartedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":game-started";

	private final String gameName;
	private final String initialTileCode;
	private final String currentTileCode;
	private final Integer totalTiles;

	public ServerToClient_GameStartedEvent(final Game game)
	{
		this(//
				game.getName(), // 
				GameUtil.getInitialTile(game).getCode(), // 
				game.getCurrentTile().getCode(), //
				game.getSequence().getTotalNumberOfTiles());
	}

	private ServerToClient_GameStartedEvent( //
			final String gameName, // 
			final String initialTileCode, //
			final String currentTileCode, //
			final Integer totalTiles)
	{
		this.gameName = gameName;
		this.initialTileCode = initialTileCode;
		this.currentTileCode = currentTileCode;
		this.totalTiles = totalTiles;
	}

	@Override
	public void dispatch(final IListener listener)
	{
		listener.onStart(this);
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

	public final static ICoder<ServerToClient_GameStartedEvent> CODER = new AbstractCoder<ServerToClient_GameStartedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_GameStartedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);

			return new ServerToClient_GameStartedEvent( //
					new String(Util.decode(tokens[0])), //
					tokens[1], tokens[2], Integer.parseInt(tokens[3]));
		}

		@Override
		public String encode(final ServerToClient_GameStartedEvent event)
		{
			return PREFIX // 
					+ ":" + Util.encode(event.getGameName()) // 
					+ ":" + event.getInitialTileCode()//
					+ ":" + event.getCurrentTileCode()//
					+ ":" + String.valueOf(event.getTotalNumberOfTiles());
		}
	};
}
