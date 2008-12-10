package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.transport.INode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ServerUtil
{
	public static Collection<ISlot> getSlotsByStatus(final IServer server, final ISlot.State... states)
	{
		return Collections2.filter(server.getSlots(), new Predicate<ISlot>()
		{
			@Override
			public boolean apply(ISlot slot)
			{
				return (Arrays.binarySearch(states, slot.getState()) > 0);
			}
		});
	}

	public static List<Player> getPlayers(IServer server)
	{
		return Lists.transform(new ArrayList<ISlot>(getSlotsByStatus(server, ISlot.State.ACCEPTED)), // 
				new Function<ISlot, Player>()
				{
					@Override
					public Player apply(ISlot slot)
					{
						return slot.getPlayer();
					}
				});
	}

	public static IRemoteClient getClientByNode(IServer server, INode node)
	{
		for (IRemoteClient client : server.getClients())
		{
			if (client.getNode() == node) return client;
		}
		
		return null;
	}
}
