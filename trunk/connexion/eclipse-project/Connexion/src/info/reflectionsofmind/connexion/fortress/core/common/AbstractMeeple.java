package info.reflectionsofmind.connexion.fortress.core.common;

public abstract class AbstractMeeple
{
	private final Player player;

	public AbstractMeeple(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return this.player;
	}
}
