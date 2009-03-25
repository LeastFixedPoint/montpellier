package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.server.IServerCoder;

public interface IServerGame<TInitInfo extends IServerInitInfo, TChange extends IChange, TAction extends IAction, TListener extends IServerGame.IListener>
{
	void start(TInitInfo initInfo);

	void onAction(TAction action);

	void addListener(TListener listener);
	
	IServerCoder getCoder();

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}
}
