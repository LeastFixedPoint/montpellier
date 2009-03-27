package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.platform.core.client.IClientCoder;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

public interface IClientGame
{
	void start(IStartInfo startInfo);

	void onChange(IChange change);

	void addListener(IListener listener);

	IClientCoder getCoder();

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}
}
