package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_ClientStateChangedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":client-state-changed";

	private final int clientIndex;
	private final State newState;

	public ServerToClient_ClientStateChangedEvent(IServer server, IRemoteClient client)
	{
		this(server.getClients().indexOf(client), client.getClient().getState());
	}

	public ServerToClient_ClientStateChangedEvent(int clientIndex, State newState)
	{
		this.clientIndex = clientIndex;
		this.newState = newState;
	}

	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onClientStateChanged(this);
	}

	public int getClientIndex()
	{
		return this.clientIndex;
	}
	
	public State getNewState()
	{
		return this.newState;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_ClientStateChangedEvent> CODER = new AbstractCoder<ServerToClient_ClientStateChangedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_ClientStateChangedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new ServerToClient_ClientStateChangedEvent(Integer.parseInt(tokens[0]), State.valueOf(tokens[1]));
		}

		@Override
		public String encode(ServerToClient_ClientStateChangedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()), event.getNewState().toString());
		}
	};
}
