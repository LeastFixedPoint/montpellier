package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;

public final class EndTurnAction extends AbstractAction
{
	public EndTurnAction(Player player)
	{
		super(player);
	}

	@Override
	public void dispatch(ServerGame serverFortress)
	{
		serverFortress.onEndTurnAction(this);
	}
}
