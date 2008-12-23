package info.reflectionsofmind.connexion.common.event.cts;

import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ClientToServer_ClientConnectionRequestEvent extends ClientToServerEvent
{
	private final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":connection-request";

	private final String playerName;

	public ClientToServer_ClientConnectionRequestEvent(final String playerName)
	{
		this.playerName = playerName;
	}
	
	@Override
	public void dispatch(INode origin, IClientToServerEventListener target)
	{
		target.onClientConnectionRequestEvent(origin, this);
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

	public final static ICoder<ClientToServer_ClientConnectionRequestEvent> CODER = // 
	new AbstractCoder<ClientToServer_ClientConnectionRequestEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_ClientConnectionRequestEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final String playerName = Util.decode(tokens[0]);
			return new ClientToServer_ClientConnectionRequestEvent(playerName);
		}

		@Override
		public String encode(final ClientToServer_ClientConnectionRequestEvent event)
		{
			return join(PREFIX, Util.encode(event.getPlayerName()));
		}
	};
}
