package info.reflectionsofmind.connexion.local.common;

import info.reflectionsofmind.connexion.core.game.Player;

public class AcceptedClient
{
	private final ConnectedClient client;
	private final Player player;

	public AcceptedClient(ConnectedClient client, Player player)
	{
		this.client = client;
		this.player = player;
	}

	public ConnectedClient getClient()
	{
		return this.client;
	}

	public Player getPlayer()
	{
		return this.player;
	}
}
