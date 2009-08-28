package info.reflectionsofmind.connexion.platform.core.common.game;

import info.reflectionsofmind.connexion.util.convert.ICoder;

public interface IGame
{
	ICoder<IAction> getActionCoder();
	
	ICoder<IStartInfo> getStartInfoCoder();
	
	ICoder<IChange> getChangeCoder();
}
