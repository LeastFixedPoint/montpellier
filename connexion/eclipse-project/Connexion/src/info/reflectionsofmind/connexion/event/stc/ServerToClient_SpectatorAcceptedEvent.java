package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.common.SpectatingClient;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_SpectatorAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":spectator-accepted";

	private final int clientIndex;

	public ServerToClient_SpectatorAcceptedEvent(IServer server, SpectatingClient spectator)
	{
		this(ServerUtil.getClients(server).indexOf(spectator.getConnectedClient().getClient()));
	}

	public ServerToClient_SpectatorAcceptedEvent(int clientIndex)
	{
		this.clientIndex = clientIndex;
	}

	@Override
	public void dispatch(IListener listener)
	{
		listener.onSpectatorAccepted(this);
	}

	public int getClientIndex()
	{
		return this.clientIndex;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_SpectatorAcceptedEvent> CODER = new AbstractCoder<ServerToClient_SpectatorAcceptedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_SpectatorAcceptedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new ServerToClient_SpectatorAcceptedEvent(Integer.parseInt(tokens[0]));
		}

		@Override
		public String encode(ServerToClient_SpectatorAcceptedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()));
		}
	};
}
