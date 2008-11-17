package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

public interface IClient
{
	void onTurn(Turn turn);
	void onStart(Player player);
	void onEnd();
	String getName();
}
