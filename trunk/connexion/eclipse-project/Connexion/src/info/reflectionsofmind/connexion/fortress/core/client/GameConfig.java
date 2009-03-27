package info.reflectionsofmind.connexion.fortress.core.client;

import info.reflectionsofmind.connexion.platform.core.common.game.IGameConfig;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.IDecoder;

public final class GameConfig implements IGameConfig
{
	public GameConfig()
	{
	}

	public final class Coder extends AbstractCoder<GameConfig>
	{
		@Override
		public boolean accepts(String string)
		{
			return true;
		}

		@Override
		public GameConfig decode(String string)
		{
			return new GameConfig();
		}

		@Override
		public String encode(GameConfig initInfo)
		{
			return "";
		}
	}

	public static final class Decoder implements IDecoder<GameConfig>
	{
		@Override
		public boolean accepts(String string)
		{
			return true;
		}

		@Override
		public GameConfig decode(String string)
		{
			return new GameConfig();
		}
	}
}
