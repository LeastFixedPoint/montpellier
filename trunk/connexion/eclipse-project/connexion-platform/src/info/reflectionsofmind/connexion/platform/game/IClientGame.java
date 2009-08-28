package info.reflectionsofmind.connexion.platform.game;

import java.util.List;

public interface IClientGame
{
	IPlayer getThisPlayer();
	
	List<IPlayer> getPlayers();
	
	void apply(IChange change);
	
	boolean isOver();
}
