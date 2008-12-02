package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public class ClientToServer_ClientConnectionRequestEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":connection-request";
	public final static Coder CODER = new Coder();

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
	public String encode()
	{
		return CODER.encode(this);
	}
	
	public static class Coder extends AbstractCoder<ClientToServer_ClientConnectionRequestEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_ClientConnectionRequestEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new ClientToServer_ClientConnectionRequestEvent(playerName);
		}

		@Override
		public String encode(ClientToServer_ClientConnectionRequestEvent event)
		{
			return join(PREFIX, Util.encode(event.getPlayerName()));
		}
	}
}
