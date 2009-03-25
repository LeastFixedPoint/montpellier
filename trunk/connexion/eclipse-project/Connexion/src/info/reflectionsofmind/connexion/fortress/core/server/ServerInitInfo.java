package info.reflectionsofmind.connexion.fortress.core.server;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerInitInfo;

import java.util.List;

public final class ServerInitInfo implements IServerInitInfo
{
	private final ITileSequence tileSequence;
	private final List<Player> players;

	public ServerInitInfo(final ITileSequence tileSequence, final List<Player> players)
	{
		this.tileSequence = tileSequence;
		this.players = players;
	}

	public ITileSequence getTileSequence()
	{
		return this.tileSequence;
	}

	public List<Player> getPlayers()
	{
		return this.players;
	}
}
