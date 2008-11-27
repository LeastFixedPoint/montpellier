package info.reflectionsofmind.connexion.local.client;

import java.util.List;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IClient extends IRemoteServer.IListener
{
	String getName();
	Game getGame();
	Player getPlayer();
	ITileSource getTileSource();
	IRemoteServer getServer();
	List<Player> getPlayers();
	
	void connect(IRemoteServer server) throws ServerConnectionException, RemoteServerException;
}
