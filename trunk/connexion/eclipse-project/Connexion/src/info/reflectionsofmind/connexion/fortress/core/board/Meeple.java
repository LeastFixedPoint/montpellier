package info.reflectionsofmind.connexion.fortress.core.board;

import info.reflectionsofmind.connexion.fortress.core.game.Player;

public class Meeple
{
	public enum Type
	{
		MEEPLE
	}

	private final Type type;
	private final Player player;

	public Meeple(final Type type, final Player player)
	{
		this.type = type;
		this.player = player;
	}

	public Type getType()
	{
		return this.type;
	}

	public Player getPlayer()
	{
		return this.player;
	}
}
