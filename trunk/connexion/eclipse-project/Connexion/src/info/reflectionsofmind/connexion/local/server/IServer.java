package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IServer extends IRemoteClient.IListener
{
	void startGame(String name);
	Game getGame();
	ITileSource getTileSource();
}
