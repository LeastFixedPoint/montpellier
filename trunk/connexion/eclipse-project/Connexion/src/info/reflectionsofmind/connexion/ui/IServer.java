package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.game.Turn;

public interface IServer
{
	void register(IClient client);
	void sendTurn(Turn turn);
	void startGame();
}
