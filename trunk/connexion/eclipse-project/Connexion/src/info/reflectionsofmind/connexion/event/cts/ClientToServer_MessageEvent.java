package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient.IListener;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ClientToServer_MessageEvent extends ClientToServerEvent
{
	public final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":message";

	private final String message;

	public ClientToServer_MessageEvent(final String message)
	{
		this.message = message;
	}
	
	@Override
	public void dispatch(IRemoteClient sender, IListener listener)
	{
		listener.onMessage(sender, this);
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

	public final static ICoder<ClientToServer_MessageEvent> CODER = new AbstractCoder<ClientToServer_MessageEvent>()
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_MessageEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String message = Util.decode(tokens[0]);
			return new ClientToServer_MessageEvent(message);
		}

		@Override
		public String encode(ClientToServer_MessageEvent event)
		{
			return join(PREFIX, Util.encode(event.getMessage()));
		}
	};
}
