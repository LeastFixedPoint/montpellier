package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_PlayerAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":participant-accepted";

	private final int clientIndex;
	private final String playerName;

	public ServerToClient_PlayerAcceptedEvent(IServer server, Player player)
	{
		this(ServerUtil.getClients(server).indexOf(ServerUtil.getSlotByPlayer(server, player).getClient()), // 
				player.getName());
	}

	public ServerToClient_PlayerAcceptedEvent(int clientIndex, String playerName)
	{
		this.clientIndex = clientIndex;
		this.playerName = playerName;
	}

	@Override
	public void dispatch(IListener listener)
	{
		listener.onParticipantAccepted(this);
	}

	public int getClientIndex()
	{
		return this.clientIndex;
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

	public final static ICoder<ServerToClient_PlayerAcceptedEvent> CODER = new AbstractCoder<ServerToClient_PlayerAcceptedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerAcceptedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new ServerToClient_PlayerAcceptedEvent(Integer.parseInt(tokens[0]), Util.decode(tokens[1]));
		}

		@Override
		public String encode(ServerToClient_PlayerAcceptedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()), Util.encode(event.getPlayerName()));
		}
	};
}
