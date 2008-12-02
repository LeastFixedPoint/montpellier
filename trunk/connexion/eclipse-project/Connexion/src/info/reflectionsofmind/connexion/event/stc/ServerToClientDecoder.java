package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ServerToClientDecoder
{
	private final static List<ICoder<?>> CODERS = // 
	ImmutableList.<ICoder<?>> of( //
			new ServerToClient_ConnectionAcceptedEvent.Coder(), // 
			new ServerToClient_PlayerDisconnectedEvent.Coder(), //
			new ServerToClient_GameStartedEvent.Coder(), //
			new ServerToClient_MessageEvent.Coder(), //
			new ServerToClient_PlayerConnectedEvent.Coder(), //
			new ServerToClient_TurnEvent.Coder());

	public ServerToClientEvent decode(String string)
	{
		for (ICoder<?> coder : CODERS)
		{
			if (coder.accepts(string))
			{
				return (ServerToClientEvent) coder.decode(string);
			}
		}

		throw new RuntimeException("Cannot decode string [" + string + "] as a server-to-client event.");
	}
}
