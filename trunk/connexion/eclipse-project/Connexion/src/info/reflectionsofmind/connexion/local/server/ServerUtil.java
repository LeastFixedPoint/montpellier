package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ServerUtil
{
	public static Collection<ISlot> getConnectedSlots(IServer server)
	{
		return Collections2.filter(server.getSlots(), new Predicate<ISlot>()
		{
			@Override
			public boolean apply(ISlot slot)
			{
				return slot.getState() == ISlot.State.CONNECTED;
			}
		});
	}

	public static List<Player> getPlayers(IServer server)
	{
		return Lists.transform(new ArrayList<ISlot>(getConnectedSlots(server)), // 
				new Function<ISlot, Player>()
				{
					@Override
					public Player apply(ISlot slot)
					{
						return slot.getPlayer();
					}
				});
	}

	public static List<String> getPlayerNames(IServer server)
	{
		return Lists.transform(getPlayers(server), // 
				new Function<Player, String>()
				{
					@Override
					public String apply(Player player)
					{
						return player.getName();
					}
				});
	}
}
