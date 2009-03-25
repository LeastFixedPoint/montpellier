package info.reflectionsofmind.connexion.fortress.core;

import info.reflectionsofmind.connexion.platform.core.client.game.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.core.server.game.IClientGameFactory;

public class FortressGameFactory implements IClientGameFactory<ClientGame>, IServerGameFactory<ServerGame>
{
	public ClientGame createClientGame()
	{
		return new ClientGame();
	}

	public ServerGame createServerGame()
	{
		return new ServerGame();
	}
}
