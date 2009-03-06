package info.reflectionsofmind.connexion.platform.common.event.cts;

import info.reflectionsofmind.connexion.platform.transport.IClientNode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ClientToServer_ChatMessageEvent extends ClientToServerEvent
{
	public final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":message";

	private final String message;

	public ClientToServer_ChatMessageEvent(final String message)
	{
		this.message = message;
	}
	
	@Override
	public void dispatch(IClientNode from, IClientToServerEventListener target)
	{
		target.onMessageEvent(from, this);
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ClientToServer_ChatMessageEvent> CODER = new AbstractCoder<ClientToServer_ChatMessageEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_ChatMessageEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String message = Util.decode(tokens[0]);
			return new ClientToServer_ChatMessageEvent(message);
		}

		@Override
		public String encode(ClientToServer_ChatMessageEvent event)
		{
			return join(PREFIX, Util.encode(event.getMessage()));
		}
	};
}
