package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

public interface IClient
{
	void onTurn(Turn turn);
	void onStart(Player player);
	void onDisconnect();
	void onEnd();
	String getName();
}
