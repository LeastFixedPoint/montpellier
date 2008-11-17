package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.board.InvalidLocationException;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Turn;

public interface IServer
{
	void register(IClient client) throws ServerException;
	void sendTurn(Turn turn) throws InvalidLocationException, NotYourTurnException;
	void startGame() throws ServerException;
}
