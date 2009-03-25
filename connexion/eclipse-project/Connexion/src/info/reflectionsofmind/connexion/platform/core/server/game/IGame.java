package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;

public interface IGame<TInit extends IInitInfo<?>, TAction extends IAction<?>, TResult extends IResult<?>>
{
	void start(TInit initInfo);
	void onAction(IPlayer player, TAction action);
	boolean isFinished();
	TResult getResult();
	void addListener(IListener listener);

	public interface IListener
	{
		void onAfterStarted();
	}
}
