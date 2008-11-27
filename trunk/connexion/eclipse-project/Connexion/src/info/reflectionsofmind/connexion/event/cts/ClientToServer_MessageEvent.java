package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

public class ClientToServer_MessageEvent extends ClientToServerEvent<ClientToServer_MessageEvent>
{
	private final String message;

	public ClientToServer_MessageEvent(final String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public IMessage<ClientToServer_MessageEvent> encode()
	{
		return new AbstractMessage<ClientToServer_MessageEvent>( //
				PREFIX + ":connection-request", Util.encode(this.message))
		{
			@Override
			public ClientToServer_MessageEvent decode()
			{
				return new ClientToServer_MessageEvent(getTokens()[0]);
			}
		};
	}
}
