package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ClientToServerDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			new ClientToServer_ClientConnectionRequestEvent.Coder(), // 
			new ClientToServer_ClientDisconnectedEvent.Coder(), //
			new ClientToServer_MessageEvent.Coder(), //
			new ClientToServer_TurnEvent.Coder());

	public ClientToServerEvent decode(String string)
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
