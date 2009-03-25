package info.reflectionsofmind.connexion.fortress.core.client;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.platform.core.common.game.IClientInitInfo;

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

}
