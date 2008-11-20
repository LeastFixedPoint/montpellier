package info.reflectionsofmind.connexion.server.local;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IServer extends IRemoteClient.IListener
{
	void startGame(String name);
	Game getGame();
	ITileSource getTileSource();
}
