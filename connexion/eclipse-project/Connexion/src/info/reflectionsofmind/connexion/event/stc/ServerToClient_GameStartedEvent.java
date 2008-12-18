package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_GameStartedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":game-started";

	private final String currentTileCode;
	private final Integer totalTiles;

	public ServerToClient_GameStartedEvent( //
			final String currentTileCode, //
			final Integer totalTiles)
	{
		this.currentTileCode = currentTileCode;
		this.totalTiles = totalTiles;
	}

	@Override
	public void dispatch(final IServerToClientEventListener listener)
	{
		listener.onStart(this);
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

			return new ServerToClient_GameStartedEvent(tokens[0], Integer.parseInt(tokens[1]));
		}

		@Override
		public String encode(final ServerToClient_GameStartedEvent event)
		{
			return PREFIX // 
					+ ":" + event.getCurrentTileCode()//
					+ ":" + String.valueOf(event.getTotalNumberOfTiles());
		}
	};
}
