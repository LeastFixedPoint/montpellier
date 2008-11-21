package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

public class ClientTurnEvent
{
	private final Turn turn;
	private final IRemoteClient client;

	public ClientTurnEvent(final IRemoteClient client, final Turn turn)
	{
		this.turn = turn;
		this.client = client;
	}

	public Turn getTurn()
	{
		return this.turn;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}
}
