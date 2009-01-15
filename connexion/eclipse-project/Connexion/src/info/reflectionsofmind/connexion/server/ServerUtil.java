package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.common.Participant;
import info.reflectionsofmind.connexion.common.Participant.State;
import info.reflectionsofmind.connexion.transport.IClientNode;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ServerUtil
{
	public static IRemoteClient getClient(IServer server, Participant client)
	{
		for (IRemoteClient remoteClient : server.getClients())
		{
			if (remoteClient.getClient() == client) return remoteClient;
		}
		
		return null;
	}
	
	public static IRemoteClient getClientByNode(IServer server, IClientNode node)
	{
		for (IRemoteClient client : server.getClients())
		{
			if (client.getNode() == node) return client;
		}

		return null;
	}

	public static List<IRemoteClient> getClientsByStates(final IServer server, final State... states)
	{
		return Arrays.asList(Collections2.filter(server.getClients(), new Predicate<IRemoteClient>()
		{
			@Override
			public boolean apply(IRemoteClient client)
			{
				return Arrays.binarySearch(states, client.getClient().getState()) >= 0;
			}
		}).toArray(new IRemoteClient[] {}));
	}

	public static List<String> mapGetName(List<IRemoteClient> clients)
	{
		return Lists.transform(clients, new Function<IRemoteClient, String>()
		{
			@Override
			public String apply(IRemoteClient client)
			{
				return client.getClient().getName();
			}
		});
	}

	public static List<State> mapGetState(List<IRemoteClient> clients)
	{
		return Lists.transform(clients, new Function<IRemoteClient, State>()
		{
			@Override
			public State apply(IRemoteClient client)
			{
				return client.getClient().getState();
			}
		});
	}
}
