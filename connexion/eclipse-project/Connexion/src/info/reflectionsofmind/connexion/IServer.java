package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IServer
{
	void register(IClient client) throws ServerException;
	void sendTurn(Turn turn) throws InvalidTileLocationException, NotYourTurnException;
	void startGame() throws ServerException;
	Game getGame();
	ITileSource getTileSource(); 
}
