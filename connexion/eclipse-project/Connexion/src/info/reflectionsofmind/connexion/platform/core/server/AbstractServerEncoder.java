package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.platform.core.server.game.IServerGame;
import info.reflectionsofmind.connexion.util.convert.IEncoder;

public abstract class AbstractServerEncoder<TServerGame extends IServerGame, T> implements IEncoder<T>
{
	private final TServerGame game;

	public AbstractServerEncoder(TServerGame game)
	{
		this.game = game;
	}
	
	public TServerGame getGame()
	{
		return this.game;
	}
}
