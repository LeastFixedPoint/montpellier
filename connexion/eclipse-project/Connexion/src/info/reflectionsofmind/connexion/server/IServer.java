package info.reflectionsofmind.connexion.server;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.core.board.exception.InvalidTileLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.NotYourTurnException;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

public interface IServer
{
	void startGame();

	void register(IClient client);
	void disconnect(IClient client);
	void sendTurn(Turn turn) throws InvalidTileLocationException, NotYourTurnException;
	
	Game getGame();
	ITileSource getTileSource();
	
	void addServerListener(IServerListener listener);
}
