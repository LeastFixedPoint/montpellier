package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

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

	public static final class Coder extends AbstractCoder<EndTurnAction>
	{
		private final ClientGame game;

		public Coder(ClientGame game)
		{
			this.game = game;
		}

		@Override
		public boolean accepts(String string)
		{
			return string.startsWith("end-turn#");
		}

		@Override
		public EndTurnAction decode(String string)
		{
			Integer playerIndex = Util.strToInt(string);
			return new EndTurnAction(playerIndex == null ? null : this.game.getPlayers().get(playerIndex));
		}

		@Override
		public String encode(EndTurnAction action)
		{
			return "end-turn#" + this.game.getPlayers().indexOf(action.getPlayer());
		}
	}
}
