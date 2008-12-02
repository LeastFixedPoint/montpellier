package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.convert.AbstractCoder;


/** Event: client has disconnected by its own initiative. */
public class ClientToServer_ClientDisconnectedEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":disconnect";
	public final static Coder CODER = new Coder();

	public enum ClientSideDisconnectReason
	{
		CLIENT_REQUEST, DESYNCHRONIZATION
	}

	private final ClientSideDisconnectReason reason;

	public ClientToServer_ClientDisconnectedEvent(final ClientSideDisconnectReason reason)
	{
		this.reason = reason;
	}

	public ClientSideDisconnectReason getReason()
	{
		return this.reason;
	}
	
	@Override
	public String encode()
	{
		return CODER.encode(this);
	}
	
	public static class Coder extends AbstractCoder<ClientToServer_ClientDisconnectedEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_ClientDisconnectedEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final ClientSideDisconnectReason reason = ClientSideDisconnectReason.valueOf(tokens[0]);
			return new ClientToServer_ClientDisconnectedEvent(reason);
		}

		@Override
		public String encode(ClientToServer_ClientDisconnectedEvent event)
		{
			return join(PREFIX, event.getReason().toString());
		}
	}
}
