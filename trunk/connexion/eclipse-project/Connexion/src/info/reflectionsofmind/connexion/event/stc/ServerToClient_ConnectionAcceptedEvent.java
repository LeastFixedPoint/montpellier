package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

import java.util.Arrays;
import java.util.List;

public class ServerToClient_ConnectionAcceptedEvent extends ServerToClientEvent<ServerToClient_ConnectionAcceptedEvent>
{
	private final List<String> existingPlayers;

	public ServerToClient_ConnectionAcceptedEvent(final List<String> existingPlayers)
	{
		this.existingPlayers = existingPlayers;
	}

	public List<String> getExistingPlayers()
	{
		return this.existingPlayers;
	}

	@Override
	public IMessage<ServerToClient_ConnectionAcceptedEvent> encode()
	{
		final String string = Util.join(Util.mapEncode(getExistingPlayers()), ",");

		return new AbstractMessage<ServerToClient_ConnectionAcceptedEvent>(PREFIX + ":connection-accepted", string)
		{
			@Override
			public ServerToClient_ConnectionAcceptedEvent decode()
			{
				final List<String> strings = Util.mapDecode(Arrays.asList(getTokens()[0].split(",")));
				return new ServerToClient_ConnectionAcceptedEvent(strings);
			}
		};
	}
}

