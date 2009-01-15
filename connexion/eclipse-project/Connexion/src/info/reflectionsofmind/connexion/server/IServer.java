package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.util.List;

public interface IServer
{
	void disconnect(IRemoteClient client, DisconnectReason server_request);
	void startGame();
	void sendMessage(String message);
	Game getGame();

	ITileSource getTileSource();
	List<IRemoteClient> getClients();
	IApplication getApplication();

	void addListener(IListener listener);

	public interface IListener
	{
		void onClientConnected(IRemoteClient client);

		void onClientDisconnected(IRemoteClient client);

		void onClientMessage(IRemoteClient client, String message);
	}
}
