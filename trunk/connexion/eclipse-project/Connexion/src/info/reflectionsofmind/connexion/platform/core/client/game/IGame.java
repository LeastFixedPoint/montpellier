package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IChange;

public interface IGame<TInitInfo extends IInitInfo<?>, TChange extends IChange<?>, TResult extends IResult<?>>
{
	void start(TInitInfo initInfo);
	void onChange(TChange change);
	boolean isFinished();
	TResult getResult();
	void addListener(IListener listener);	
	
	public interface IListener
	{
		void onAfterStarted();
	}
}
