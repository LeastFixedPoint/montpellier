package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGame;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransport;

import java.util.List;

public interface IServer extends IServerTransport.IListener
{
	void disconnect(IRemoteClient client, DisconnectReason reason);
	void startGame();
	void sendChat(String message);
	IServerGame<?, ?, ?> getGame();
	void setGameFactory(IServerGameFactory gameFactory);

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
