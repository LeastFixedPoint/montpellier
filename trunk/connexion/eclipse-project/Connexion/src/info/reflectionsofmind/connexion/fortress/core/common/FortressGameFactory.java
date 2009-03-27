package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.client.GameConfig;
import info.reflectionsofmind.connexion.fortress.core.server.ITileSequence;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGameFactory;
import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;
import info.reflectionsofmind.connexion.util.form.Form;

public class FortressGameFactory implements IClientGameFactory, IServerGameFactory
{
	@Override
	public IGameConfig decodeGameConfig(String string)
	{
		return null;
	}

	@Override
	public ClientGame createClientGame(IGameConfig gameConfig)
	{
		return new ClientGame((GameConfig) gameConfig);
	}

	@Override
	public ServerGame createServerGame(Form form)
	{
		ITileSequence tileSequence = new FixedRandomizedTileSequence();
		return new ServerGame(tileSequence);
	}

	@Override
	public Form newConfigurationForm()
	{
		return null;
	}
}
