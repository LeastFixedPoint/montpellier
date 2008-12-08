package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient.IListener;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Event: client has disconnected by its own initiative. */
public class ClientToServer_ClientDisconnectedEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":disconnect";

	public enum ClientSideDisconnectReason
	{
		CLIENT_REQUEST, DESYNCHRONIZATION
	}

	private final ClientSideDisconnectReason reason;

	public ClientToServer_ClientDisconnectedEvent(final ClientSideDisconnectReason reason)
	{
		this.reason = reason;
	}
	
	@Override
	public void dispatch(IRemoteClient sender, IListener listener)
	{
		listener.onDisconnect(sender, this);
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

	public final static ICoder<ClientToServer_ClientDisconnectedEvent> CODER = new AbstractCoder<ClientToServer_ClientDisconnectedEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_ClientDisconnectedEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final ClientSideDisconnectReason reason = ClientSideDisconnectReason.valueOf(tokens[0]);
			return new ClientToServer_ClientDisconnectedEvent(reason);
		}

		@Override
		public String encode(final ClientToServer_ClientDisconnectedEvent event)
		{
			return join(PREFIX, event.getReason().toString());
		}
	};
}
