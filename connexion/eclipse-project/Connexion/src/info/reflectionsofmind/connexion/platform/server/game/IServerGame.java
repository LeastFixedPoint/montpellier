package info.reflectionsofmind.connexion.platform.server.game;

import info.reflectionsofmind.connexion.platform.common.game.IPlayer;

public interface IServerGame<TInit extends IInitInfo<?>, TAction extends IAction<?>, TResult extends IResult<?>>
{
	void start(TInit initInfo);
	void onAction(IPlayer player, TAction action);
	boolean isFinished();
	TResult getResult();
	void addChangeListener(IChangeListener listener);
}
