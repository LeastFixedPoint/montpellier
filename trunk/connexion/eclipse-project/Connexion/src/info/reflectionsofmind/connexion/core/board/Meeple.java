package info.reflectionsofmind.connexion.core.board;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.util.Link;

public class Meeple
{
	private final Link<Meeple, Player> player = new Link<Meeple, Player>(this);

	private Meeple(final Player player)
	{
		this.player.link(player.getMeeples());
	}

	public Player getPlayer()
	{
		return this.player.get();
	}
}
