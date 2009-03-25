package info.reflectionsofmind.connexion.platform.core.client.game;

public interface IGameFactory<TGame extends IGame<?, ?, ?>>
{
	TGame createGame();
}
