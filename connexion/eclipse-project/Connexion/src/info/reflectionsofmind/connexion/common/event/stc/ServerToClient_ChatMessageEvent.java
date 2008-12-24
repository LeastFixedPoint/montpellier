package info.reflectionsofmind.connexion.common.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_ChatMessageEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":message";

	private final int playerIndex;
	private final String message;

	public ServerToClient_ChatMessageEvent(final int clientIndex, final String message)
	{
		this.playerIndex = clientIndex;
		this.message = message;
	}
	
	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onChatMessage(this);
	}

	public int getClientIndex()
	{
		return this.playerIndex;
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

	public final static ICoder<ServerToClient_ChatMessageEvent> CODER = new AbstractCoder<ServerToClient_ChatMessageEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_ChatMessageEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final int clientIndex = Integer.valueOf(tokens[0]);
			final String message = Util.decode(tokens[1]);
			return new ServerToClient_ChatMessageEvent(clientIndex, message);
		}

		@Override
		public String encode(final ServerToClient_ChatMessageEvent event)
		{
			return join(PREFIX, String.valueOf(event.getClientIndex()), Util.encode(event.message));
		}
	};
}