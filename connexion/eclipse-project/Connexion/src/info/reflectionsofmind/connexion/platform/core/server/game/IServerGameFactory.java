package info.reflectionsofmind.connexion.platform.core.server.game;

public interface IServerGameFactory
{
	IServerGame<?, ?, ?, ?> createClientGame();
}
