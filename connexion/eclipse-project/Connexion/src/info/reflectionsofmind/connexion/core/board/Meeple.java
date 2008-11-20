package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.game.Player;

public class Meeple
{
	private final Player player;

	private Meeple(final Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return this.player;
	}
}
