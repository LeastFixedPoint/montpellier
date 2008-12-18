package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** Event: client has disconnected by its own initiative. */
public class ClientToServer_DisconnectNoticeEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":disconnect";

	private final DisconnectReason reason;

	public ClientToServer_DisconnectNoticeEvent(final DisconnectReason reason)
	{
		this.reason = reason;
	}
	
	@Override
	public void dispatch(INode origin, IClientToServerEventListener target)
	{
		target.onDisconnectNoticeEvent(origin, this);
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
			final DisconnectReason reason = DisconnectReason.valueOf(tokens[0]);
			return new ClientToServer_DisconnectNoticeEvent(reason);
		}

		@Override
		public String encode(final ClientToServer_DisconnectNoticeEvent event)
		{
			return join(PREFIX, event.getReason().toString());
		}
	};
}
