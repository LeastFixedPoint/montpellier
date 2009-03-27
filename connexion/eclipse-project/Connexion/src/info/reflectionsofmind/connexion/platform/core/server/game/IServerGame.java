package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;
import info.reflectionsofmind.connexion.platform.core.server.IServerCoder;

public interface IServerGame
{
	void start(int numPlayers);

	IStartInfo getClientStartInfo(IPlayer player);

	void onAction(IAction action);

	void addListener(IListener listener);
	
	IServerCoder getCoder();

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}

}
