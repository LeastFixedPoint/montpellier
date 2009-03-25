package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGameFactory;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;

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
