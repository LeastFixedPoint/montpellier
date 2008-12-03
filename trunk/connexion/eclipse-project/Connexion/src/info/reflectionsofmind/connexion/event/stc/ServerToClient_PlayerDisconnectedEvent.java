package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Server-to-client event: a player has (been) disconnected from game. */
public class ServerToClient_PlayerDisconnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-disconnected";

	private final int playerIndex;
	private final DisconnectReason reason;

	public ServerToClient_PlayerDisconnectedEvent(final IServer server, final Player player, final DisconnectReason reason)
	{
		this(ServerUtil.getPlayers(server).indexOf(player), reason);
	}

	private ServerToClient_PlayerDisconnectedEvent(final int playerIndex, final DisconnectReason reason)
	{
		this.playerIndex = playerIndex;
		this.reason = reason;
	}

	@Override
	public void dispatch(final IListener listener)
	{
		listener.onPlayerDisconnected(this);
	}

	public int getPlayerIndex()
	{
		return this.playerIndex;
	}

	public DisconnectReason getReason()
	{
		return this.reason;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_PlayerDisconnectedEvent> CODER = new AbstractCoder<ServerToClient_PlayerDisconnectedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerDisconnectedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int playerIndex = Integer.valueOf(tokens[0]);
			final DisconnectReason reason = DisconnectReason.valueOf(tokens[1]);
			return new ServerToClient_PlayerDisconnectedEvent(playerIndex, reason);
		}

		@Override
		public String encode(final ServerToClient_PlayerDisconnectedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getPlayerIndex()), event.reason.toString());
		}
	};
}
