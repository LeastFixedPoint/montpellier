package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.common.AcceptedClient;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer.IListener;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_SpectatorRejectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":participant-rejected";
	
	public ServerToClient_SpectatorRejectedEvent(IServer server, AcceptedClient client)
	{
		this(ServerUtil.getParticipants(server).indexOf(client));
	}
	
	private final int participantIndex;

	private ServerToClient_SpectatorRejectedEvent(final int clientIndex)
	{
		this.participantIndex = clientIndex;
	}

	@Override
	public void dispatch(IListener listener)
	{
		listener.onParticipantRejected(this);
	}

	public int getParticipantIndex()
	{
		return this.participantIndex;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}
	
	public final static ICoder<ServerToClient_SpectatorRejectedEvent> CODER = new AbstractCoder<ServerToClient_SpectatorRejectedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_SpectatorRejectedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new ServerToClient_SpectatorRejectedEvent(Integer.parseInt(tokens[0]));
		}

		@Override
		public String encode(ServerToClient_SpectatorRejectedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getParticipantIndex()));
		}
	};
}

