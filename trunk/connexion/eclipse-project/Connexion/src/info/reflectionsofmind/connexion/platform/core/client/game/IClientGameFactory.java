package info.reflectionsofmind.connexion.platform.core.client.game;

public interface IClientGameFactory<TGame extends IServerGame<?, ?, ?>>
{
	TGame createServerGame();
}
