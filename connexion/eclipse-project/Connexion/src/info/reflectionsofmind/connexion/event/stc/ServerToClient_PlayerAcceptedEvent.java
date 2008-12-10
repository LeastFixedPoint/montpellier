package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_PlayerAcceptedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-accepted";

	private final int clientIndex;

	public ServerToClient_PlayerAcceptedEvent(IServer server, IRemoteClient client)
	{
		this(server.getClients().indexOf(client));
	}

	public ServerToClient_PlayerAcceptedEvent(int clientIndex)
	{
		this.clientIndex = clientIndex;
	}

	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onPlayerAccepted(this);
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
			return new ServerToClient_PlayerAcceptedEvent(Integer.parseInt(tokens[0]));
		}

		@Override
		public String encode(ServerToClient_PlayerAcceptedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()));
		}
	};
}
