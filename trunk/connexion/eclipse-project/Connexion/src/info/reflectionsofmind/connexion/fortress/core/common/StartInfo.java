package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.platform.core.client.AbstractClientDecoder;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;
import info.reflectionsofmind.connexion.util.Util;

import java.util.ArrayList;
import java.util.List;

public final class StartInfo implements IStartInfo
{
	private final List<Player> players;
	private final Player thisPlayer;

	private StartInfo(List<Player> players, Player thisPlayer)
	{
		this.players = players;
		this.thisPlayer = thisPlayer;
	}

	public List<Player> getPlayers()
	{
		return this.players;
	}

	public Player getThisPlayer()
	{
		return this.thisPlayer;
	}

	public static final class Decoder extends AbstractClientDecoder<ClientGame, StartInfo>
	{
		public Decoder(ClientGame game)
		{
			super(game);
		}

		@Override
		public boolean accepts(String string)
		{
			return true;
		}

		@Override
		public StartInfo decode(String string)
		{
			final String[] tokens = string.split("#");
			final int numPlayers = Util.strToInt(tokens[0]);
			final int thisPlayer = Util.strToInt(tokens[1]);

			final List<Player> players = new ArrayList<Player>();
			for (int i = 0; i < numPlayers; i++)
				players.add(new Player());

			return new StartInfo(players, players.get(thisPlayer));
		}
	}
}
