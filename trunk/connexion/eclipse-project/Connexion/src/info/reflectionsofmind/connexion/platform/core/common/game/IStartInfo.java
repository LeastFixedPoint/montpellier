package info.reflectionsofmind.connexion.platform.core.common.game;

import java.util.List;

public interface IStartInfo
{
	List<? extends IPlayer> getPlayers();
	IPlayer getThisPlayer();
}
