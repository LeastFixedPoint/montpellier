package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IClientInitInfo;

public interface IClientCoder
{
	String encodeAction(IAction action);

	IChange decodeChange(String string);

	IClientInitInfo decodeInitInfo(String string);
}
