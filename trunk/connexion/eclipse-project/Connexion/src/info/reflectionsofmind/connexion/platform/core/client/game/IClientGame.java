package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;

public interface IClientGame<TInitInfo extends IClientInitInfo, TAction extends IAction, TChange extends IChange, TListener extends IClientGame.IListener>
{
	void start(TInitInfo initInfo);
	void onChange(TChange change);
	void addListener(TListener listener);

	public interface IListener
	{
		void onStarted();
		void onFinished();
	}
}
