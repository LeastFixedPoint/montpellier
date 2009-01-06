package info.reflectionsofmind.connexion.common.event.stc;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ServerToClient_ChatMessageEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":message";

	private final Integer clientIndex;
	private final String message;

	public ServerToClient_ChatMessageEvent(final Integer clientIndex, final String message)
	{
		this.clientIndex = clientIndex;
		this.message = message;
	}
	
	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onChatMessage(this);
	}

	public Integer getClientIndex()
	{
		return this.clientIndex;
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
			final Integer clientIndex = tokens[0].isEmpty() ? null : Integer.valueOf(tokens[0]);
			final String message = Util.decode(tokens[1]);
			return new ServerToClient_ChatMessageEvent(clientIndex, message);
		}

		@Override
		public String encode(final ServerToClient_ChatMessageEvent event)
		{
			final String clientIndex = event.getClientIndex() == null ? "" : event.getClientIndex().toString();
			return join(PREFIX, clientIndex, Util.encode(event.message));
		}
	};
}
