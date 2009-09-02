package info.reflectionsofmind.connexion.platform.game.server;

import info.reflectionsofmind.connexion.platform.game.IAction;
import info.reflectionsofmind.connexion.platform.game.IPlayer;

import java.util.List;

public interface IServerGame
{
	List<IPlayer> getPlayers();

	void apply(IAction action);
	
	boolean isOver();
}
