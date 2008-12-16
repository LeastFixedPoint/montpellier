package info.reflectionsofmind.connexion.local.server;	

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.List;

public interface IServer
{
	void startGame(String name);
	Game getGame();
	ITileSource getTileSource();
	List<IRemoteClient> getClients();
	List<ITransport> getTransports();
	Settings getSettings();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		void onClientConnected(IRemoteClient client);
		void onClientMessage(IRemoteClient client, String message);
	}
}
