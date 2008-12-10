package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Event: client has disconnected by its own initiative. */
public class ClientToServer_DisconnectNoticeEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":disconnect";

	public enum ClientSideDisconnectReason
	{
		CLIENT_REQUEST, DESYNCHRONIZATION
	}

	private final ClientSideDisconnectReason reason;

	public ClientToServer_DisconnectNoticeEvent(final ClientSideDisconnectReason reason)
	{
		this.reason = reason;
	}
	
	@Override
	public void dispatch(INode origin, IClientToServerEventTarget target)
	{
		target.onDisconnectNoticeEvent(origin, this);
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

	public final static ICoder<ClientToServer_DisconnectNoticeEvent> CODER = new AbstractCoder<ClientToServer_DisconnectNoticeEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_DisconnectNoticeEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final ClientSideDisconnectReason reason = ClientSideDisconnectReason.valueOf(tokens[0]);
			return new ClientToServer_DisconnectNoticeEvent(reason);
		}

		@Override
		public String encode(final ClientToServer_DisconnectNoticeEvent event)
		{
			return join(PREFIX, event.getReason().toString());
		}
	};
}
