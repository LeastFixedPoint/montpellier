package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

public interface IClientCoder
{
	String encodeAction(IAction action);

	IChange decodeChange(String string);

	IGameConfig decodeGameConfig(String string);
	
	IStartInfo decodeStartInfo(String string);
}
