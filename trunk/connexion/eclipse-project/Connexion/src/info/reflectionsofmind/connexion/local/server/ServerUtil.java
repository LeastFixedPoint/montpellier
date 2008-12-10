package info.reflectionsofmind.connexion.local.server;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient.State;
import info.reflectionsofmind.connexion.transport.INode;

public class ServerUtil
{
	public static IRemoteClient getClientByNode(IServer server, INode node)
	{
		for (IRemoteClient client : server.getClients())
		{
			if (client.getNode() == node) return client;
		}

		return null;
	}

	public static IRemoteClient getClientByPlayer(IServer server, Player player)
	{
		for (IRemoteClient client : getClientsByStates(server, State.ACCEPTED))
		{
			if (client.getPlayer() == player) return client;
		}

		return null;
	}

	public static List<IRemoteClient> getClientsByStates(final IServer server, final IRemoteClient.State... states)
	{
		return Arrays.asList(Collections2.filter(server.getClients(), new Predicate<IRemoteClient>()
		{
			@Override
			public boolean apply(IRemoteClient client)
			{
				return Arrays.binarySearch(states, client.getState()) >= 0;
			}
		}).toArray(new IRemoteClient[] {}));
	}
	
	public static List<Player> getPlayers(IServer server)
	{
		return Lists.transform(getClientsByStates(server, IRemoteClient.State.ACCEPTED), new Function<IRemoteClient, Player>()
		{
			@Override
			public Player apply(IRemoteClient client)
			{
				return client.getPlayer();
			}
		});
	}
}
