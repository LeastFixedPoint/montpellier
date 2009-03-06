package info.reflectionsofmind.connexion.platform.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.transport.IServerTransport;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.util.List;

public interface IServer extends IServerTransport.IListener
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

		void onAfterClientDisconnected(IRemoteClient client);

		void onClientMessage(IRemoteClient client, String message);
	}
}
