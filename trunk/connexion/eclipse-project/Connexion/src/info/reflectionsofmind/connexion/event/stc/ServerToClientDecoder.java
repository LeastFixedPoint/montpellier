package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ServerToClientDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			ServerToClient_ConnectionAcceptedEvent.CODER, // 
			ServerToClient_PlayerDisconnectedEvent.CODER, //
			ServerToClient_GameStartedEvent.CODER, //
			ServerToClient_MessageEvent.CODER, //
			ServerToClient_PlayerConnectedEvent.CODER, //
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

		throw new RuntimeException("Cannot decode string [" + string + "] as a server-to-client event.");
	}
}
