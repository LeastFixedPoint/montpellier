package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IPlayer;

public abstract class AbstractAction implements IAction
{
	private final Player player;

	public AbstractAction(Player player)
	{
		this.player = player;
	}
	
	@Override
	public IPlayer getPlayer()
	{
		return this.player;
	}
	
	public abstract void dispatch(ServerGame serverFortress);
}
