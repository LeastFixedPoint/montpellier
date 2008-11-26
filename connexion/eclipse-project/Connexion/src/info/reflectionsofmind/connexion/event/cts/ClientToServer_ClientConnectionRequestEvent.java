package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

public class ClientToServer_ClientConnectionRequestEvent extends ClientToServerEvent<ClientToServer_ClientConnectionRequestEvent>
{
	private final String playerName;

	public ClientToServer_ClientConnectionRequestEvent(final String playerName)
	{
		this.playerName = playerName;
	}

	public String getPlayerName()
	{
		return this.playerName;
	}

	@Override
	public IMessage<ClientToServer_ClientConnectionRequestEvent> encode()
	{
		return new AbstractMessage<ClientToServer_ClientConnectionRequestEvent>( //
				PREFIX + ":connection-request", playerName)
		{
			@Override
			public ClientToServer_ClientConnectionRequestEvent decode()
			{
				return new ClientToServer_ClientConnectionRequestEvent(getTokens()[0]);
			}
		};
	}
}
