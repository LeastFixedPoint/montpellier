package info.reflectionsofmind.connexion.platform.core.common.event.stc;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Server-to-client event: a client has (been) disconnected from game. */
public class ServerToClient_ClientDisconnectedEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":client-disconnected";

	private final int clientIndex;
	private final DisconnectReason reason;

	public ServerToClient_ClientDisconnectedEvent(final int clientIndex, final DisconnectReason reason)
	{
		this.clientIndex = clientIndex;
		this.reason = reason;
	}

	@Override
	public void dispatch(final IServerToClientEventListener listener)
	{
		listener.onClientDisconnected(this);
	}

	public int getClientIndex()
	{
		return this.clientIndex;
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

	public final static ICoder<ServerToClient_ClientDisconnectedEvent> CODER = new AbstractCoder<ServerToClient_ClientDisconnectedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_ClientDisconnectedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int clientIndex = Integer.valueOf(tokens[0]);
			final DisconnectReason reason = DisconnectReason.valueOf(tokens[1]);
			return new ServerToClient_ClientDisconnectedEvent(clientIndex, reason);
		}

		@Override
		public String encode(final ServerToClient_ClientDisconnectedEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()), event.reason.toString());
		}
	};
}
