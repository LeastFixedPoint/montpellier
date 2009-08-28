package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IGame;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

public interface IServerGame<TListener extends IServerGame.IListener> extends IGame
{
	void start(int numPlayers);

	IStartInfo getClientStartInfo(IPlayer player);

	void onAction(IAction action);

	void addListener(TListener listener);

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}
}
