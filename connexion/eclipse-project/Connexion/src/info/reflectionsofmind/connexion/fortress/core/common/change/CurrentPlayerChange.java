package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.AbstractClientDecoder;
import info.reflectionsofmind.connexion.platform.core.server.AbstractServerEncoder;
import info.reflectionsofmind.connexion.util.Util;

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

	public static final class Encoder extends AbstractServerEncoder<ServerGame, CurrentPlayerChange>
	{
		public Encoder(final ServerGame game)
		{
			super(game);
		}

		@Override
		public String encode(final CurrentPlayerChange change)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("player-changed");
			builder.append("#").append(getGame().getPlayers().indexOf(change.getNextPlayer()));

			return builder.toString();
		}
	}

	public static final class Decoder extends AbstractClientDecoder<ClientGame, CurrentPlayerChange>
	{
		public Decoder(final ClientGame game)
		{
			super(game);
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("player-changed#");
		}

		@Override
		public CurrentPlayerChange decode(final String string)
		{
			final String[] strings = string.split("#");
			return new CurrentPlayerChange(getGame().getPlayers().get(Util.strToInt(strings[1])));
		}
	}
}
