package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;

public interface IServerGame<TInitInfo extends IServerInitInfo, TChange extends IChange, TAction extends IAction, TListener extends IServerGame.IListener>
{
	void start(TInitInfo initInfo);

	void onAction(TAction action);

	void addListener(TListener listener);

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}
}
