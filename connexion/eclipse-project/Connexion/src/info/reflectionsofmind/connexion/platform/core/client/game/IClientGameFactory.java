package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IClientInitInfo;

public interface IClientGameFactory<TClientGame extends IClientGame<IClientInitInfo, IAction, IChange, IClientGame.IListener>>
{
	TClientGame createClientGame();
}