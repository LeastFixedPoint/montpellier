package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ServerUtil
{
	public static Collection<ISlot<?,?>> getConnectedSlots(IServer server)
	{
		return Collections2.filter(server.getSlots(), new Predicate<ISlot<?,?>>()
		{
			@Override
			public boolean apply(ISlot<?,?> slot)
			{
				return slot.getState() == ISlot.State.CONNECTED;
			}
		});
	}

	public static List<Player> getPlayers(IServer server)
	{
		return Lists.transform(new ArrayList<ISlot<?,?>>(getConnectedSlots(server)), // 
				new Function<ISlot<?,?>, Player>()
				{
					@Override
					public Player apply(ISlot<?,?> slot)
					{
						return slot.getPlayer();
					}
				});
	}

	public static List<IRemoteClient<?,?>> getClients(IServer server)
	{
		return Lists.transform(new ArrayList<ISlot<?,?>>(getConnectedSlots(server)), // 
				new Function<ISlot<?,?>, IRemoteClient<?,?>>()
				{
					@Override
					public IRemoteClient<?,?> apply(ISlot<?,?> slot)
					{
						return slot.getClient();
					}
				});
	}

	
	public static ISlot<?,?> getSlotByClient(IServer server, IRemoteClient<?,?> client)
	{
		for (ISlot<?,?> slot : getConnectedSlots(server))
		{
			if (slot.getClient() == client) return slot;
		}
		
		throw new RuntimeException("Slot not found for client [" + client + "].");
	}

	@SuppressWarnings("unchecked")
	public static <T> T findTransport(final IServer server, final Class<T> transportClass)
	{
		for (final ITransport<?> transport : server.getTransports())
		{
			if (transportClass.isInstance(transport))
			{
				return (T) transport;
			}
		}
	
		return null;
	}
}
