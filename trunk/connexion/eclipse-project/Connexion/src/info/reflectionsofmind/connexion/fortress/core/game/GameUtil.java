package info.reflectionsofmind.connexion.fortress.core.game;

import info.reflectionsofmind.connexion.fortress.core.board.Board;
import info.reflectionsofmind.connexion.fortress.core.board.BoardUtil;
import info.reflectionsofmind.connexion.fortress.core.board.Feature;
import info.reflectionsofmind.connexion.fortress.core.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.tile.Tile;
import info.reflectionsofmind.connexion.util.Util;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class GameUtil
{
	public static Map<Player, List<Meeple>> getMeeplesByPlayer(final Game game, final Feature feature)
	{
		final List<Meeple> meeplesOnFeature = BoardUtil.getMeeplesOnFeature(game.getBoard(), feature);

		return Util.group(meeplesOnFeature, new Function<Meeple, Player>()
		{
			@Override
			public Player apply(final Meeple meeple)
			{
				return meeple.getPlayer();
			}
		});
	}

	public static Meeple getFreeMeepleOfType(final Board board, final Player player, final Meeple.Type type)
	{
		for (final Meeple meeple : player.getMeeples())
		{
			if (!board.getMeeples().contains(meeple) && meeple.getType() == type)
			{
				return meeple;
			}
		}

		return null;
	}

	public static Tile getInitialTile(final Game game)
	{
		return game.getBoard().getPlacements().get(0).getTile();
	}

	public static List<String> getNames(final List<Player> players)
	{
		return Lists.transform(players, // 
				new Function<Player, String>()
				{
					@Override
					public String apply(final Player player)
					{
						return player.getName();
					}
				});
	}
}
