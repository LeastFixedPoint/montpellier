package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.game.Turn;

public interface IServerListener
{
	void onGameStart();
	void onGameEnd();
	void onClientConnect(IClient client);
	void onClientDisconnect(IClient client);
	void onTurn(Turn turn);
}
