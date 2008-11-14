package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.game.Turn;

public interface IServer
{
	void register(IClient client) throws ServerException;
	void sendTurn(Turn turn) throws ServerException;
	void startGame() throws ServerException;
}
