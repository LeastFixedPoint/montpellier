package info.reflectionsofmind.connexion.platform.core.server.game;

public interface IGameFactory<TGame extends IGame<?, ?, ?>>
{
	TGame createGame();
}
