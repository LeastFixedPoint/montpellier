package info.reflectionsofmind.connexion.common.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** STC event: another client has connected to the server. */
public class ServerToClient_ClientConnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":client-connected";

	private final String clientName;

	public ServerToClient_ClientConnectedEvent(final String clientName)
	{
		this.clientName = clientName;
	}
	
	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onClientConnected(this);
	}

	public String getClientName()
	{
		return this.clientName;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_ClientConnectedEvent> CODER = new AbstractCoder<ServerToClient_ClientConnectedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_ClientConnectedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new ServerToClient_ClientConnectedEvent(playerName);
		}

		@Override
		public String encode(final ServerToClient_ClientConnectedEvent event)
		{
			return join(PREFIX, Util.encode(event.getClientName()));
		}
	};
}
