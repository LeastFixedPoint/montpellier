package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_PlayerRejectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":player-rejected";
	
	private final int clientIndex;

	public ServerToClient_PlayerRejectedEvent(final int clientIndex)
	{
		this.clientIndex = clientIndex;
	}

	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onPlayerRejected(this);
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
	
	public final static ICoder<ServerToClient_PlayerRejectedEvent> CODER = new AbstractCoder<ServerToClient_PlayerRejectedEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_PlayerRejectedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			return new ServerToClient_PlayerRejectedEvent(Integer.parseInt(tokens[0]));
		}

		@Override
		public String encode(ServerToClient_PlayerRejectedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()));
		}
	};
}

