package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ClientToServerEventDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			ClientToServer_ClientConnectionRequestEvent.CODER, // 
			ClientToServer_DisconnectNoticeEvent.CODER, //
			ClientToServer_ChatMessageEvent.CODER, //
			ClientToServer_TurnEvent.CODER);

	public static ClientToServerEvent decode(String string)
	{
		for (ICoder<?> coder : CODERS)
		{
			if (coder.accepts(string))
			{
				return (ClientToServerEvent) coder.decode(string);
			}
		}
		
		throw new RuntimeException("Cannot decode string [" + string + "] as a client-to-server event.");
	}
}
