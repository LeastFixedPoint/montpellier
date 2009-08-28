package info.reflectionsofmind.connexion.platform.game;

import java.util.List;

public interface IServerGame
{
	List<IPlayer> getPlayers();

	void apply(IAction action);
	
	boolean isOver();
}
