package info.reflectionsofmind.connexion.fortress.core.client;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.platform.core.common.game.IClientInitInfo;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

import java.util.ArrayList;
import java.util.List;

public final class ClientInitInfo implements IClientInitInfo
{
	private final Tile firstTile;
	private final List<Player> players;

	public ClientInitInfo(final Tile firstTile, final List<Player> players)
	{
		this.firstTile = firstTile;
		this.players = players;
	}

	public Tile getFirstTile()
	{
		return this.firstTile;
	}

	public List<Player> getPlayers()
	{
		return this.players;
	}

	public final class Coder extends AbstractCoder<ClientInitInfo>
	{
		@Override
		public boolean accepts(String string)
		{
			return true;
		}

		@Override
		public ClientInitInfo decode(String string)
		{
			final String[] strings = string.split(";");

			final String firstTile = strings[0];
			final List<Player> players = new ArrayList<Player>();

			for (int i = 1; i < strings.length; i++)
				players.add(new Player());

			try
			{
				return new ClientInitInfo(new Tile(firstTile), players);
			}
			catch (TileCodeFormatException exception)
			{
				throw new RuntimeException(exception);
			}
		}

		@Override
		public String encode(ClientInitInfo initInfo)
		{
			StringBuilder builder = new StringBuilder();
			builder.append(initInfo.getFirstTile());

			for (@SuppressWarnings("unused")
			Player player : initInfo.getPlayers())
				builder.append(";");

			return builder.toString();
		}
	}
}
