package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.client.remote.IRemoteServer;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IClient extends IRemoteServer.IListener
{
	String getName();
	Game getGame();
	Player getPlayer();
	ITileSource getTileSource();
	IRemoteServer getServer();
}
