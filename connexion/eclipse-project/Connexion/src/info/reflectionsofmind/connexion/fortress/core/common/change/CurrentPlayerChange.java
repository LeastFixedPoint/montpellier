package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;

public final class CurrentPlayerChange extends AbstractChange
{
	private final Player nextPlayer;

	private CurrentPlayerChange(Player nextPlayer)
	{
		this.nextPlayer = nextPlayer;
	}

	public Player getNextPlayer()
	{
		return this.nextPlayer;
	}
	
	@Override
	public void dispatch(ClientGame game)
	{
		game.onCurrentPlayerChanged(this);
	}
}
