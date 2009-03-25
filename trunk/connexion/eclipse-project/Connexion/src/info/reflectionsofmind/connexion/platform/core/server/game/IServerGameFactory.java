package info.reflectionsofmind.connexion.platform.core.server.game;

public interface IServerGameFactory<TServerGame extends IServerGame<?,?,?,?>>
{
	TServerGame createServerGame();
}
