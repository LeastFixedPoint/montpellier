package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

public interface IServerCoder
{
	IAction decodeAction(String string);
	
	String encodeChange(IChange change);
	
	String encodeInitInfo(IGameConfig initInfo);
	
	String encodeStartInfo(IStartInfo startInfo);
}
