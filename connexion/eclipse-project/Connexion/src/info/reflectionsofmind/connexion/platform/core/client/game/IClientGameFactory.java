package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;

public interface IClientGameFactory
{
	IGameConfig decodeGameConfig(String string);
	IClientGame createClientGame(IGameConfig gameConfig);
}
