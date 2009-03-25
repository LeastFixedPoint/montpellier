package info.reflectionsofmind.connexion.platform.core.common.event.stc;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ServerToClientEventDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			ServerToClient_ConnectionAcceptedEvent.CODER, //
			ServerToClient_ClientConnectedEvent.CODER, //
			ServerToClient_ClientStateChangedEvent.CODER, // 
			ServerToClient_ClientDisconnectedEvent.CODER, //
			ServerToClient_ChatMessageEvent.CODER, //
			ServerToClient_GameStartedEvent.CODER, //
			ServerToClient_TurnEvent.CODER);

	public static ServerToClientEvent decode(String string)
	{
		for (ICoder<?> decoder : CODERS)
		{
			if (decoder.accepts(string))
			{
				return (ServerToClientEvent) decoder.decode(string);
			}
		}

		throw new RuntimeException("Coder not found for [" + string + "] to decode it as a CtS event.");
	}
}
