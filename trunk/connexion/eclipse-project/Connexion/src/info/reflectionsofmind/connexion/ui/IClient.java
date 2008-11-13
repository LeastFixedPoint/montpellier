package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

public interface IClient
{
	void onTurn(Turn turn);
	void onStart(Game game, Player player);
	String getName();
}
