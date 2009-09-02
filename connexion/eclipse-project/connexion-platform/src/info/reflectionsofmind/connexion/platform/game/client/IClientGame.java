package info.reflectionsofmind.connexion.platform.game.client;

import info.reflectionsofmind.connexion.platform.game.IChange;
import info.reflectionsofmind.connexion.platform.game.IPlayer;

import java.util.List;

public interface IClientGame
{
	IPlayer getThisPlayer();
	
	List<IPlayer> getPlayers();
	
	void apply(IChange change);
	
	boolean isOver();
}
