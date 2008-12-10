package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.List;

public interface IClient
{
	String getName();
	Game getGame();
	Player getPlayer();
	ITileSource getTileSource();
	IRemoteServer getServer();
	List<Player> getPlayers();
	List<ITransport> getTransports();
	Settings getSettings();

	void connect(IRemoteServer server) throws ServerConnectionException, RemoteServerException;
}
