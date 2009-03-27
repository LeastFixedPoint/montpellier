package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.platform.core.client.game.IClientGame;
import info.reflectionsofmind.connexion.util.convert.IDecoder;

public abstract class AbstractClientDecoder<TClientGame extends IClientGame, T> implements IDecoder<T>
{
	private final TClientGame game;

	public AbstractClientDecoder(TClientGame game)
	{
		this.game = game;
	}
	
	public TClientGame getGame()
	{
		return this.game;
	}
}
