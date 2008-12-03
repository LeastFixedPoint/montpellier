package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** STC event: another player has connected to the game. */
public class ServerToClient_PlayerConnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-connected";

	private final String playerName;

	public ServerToClient_PlayerConnectedEvent(final Player player)
	{
		this(player.getName());
	}
	
	private ServerToClient_PlayerConnectedEvent(final String playerName)
	{
		this.playerName = playerName;
	}
	
	@Override
	public void dispatch(IListener listener)
	{
		listener.onPlayerConnected(this);
	}

	public String getPlayerName()
	{
		return this.playerName;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_PlayerConnectedEvent> CODER = new AbstractCoder<ServerToClient_PlayerConnectedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerConnectedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new ServerToClient_PlayerConnectedEvent(playerName);
		}

		@Override
		public String encode(final ServerToClient_PlayerConnectedEvent event)
		{
			return join(PREFIX, Util.encode(event.getPlayerName()));
		}
	};
}
