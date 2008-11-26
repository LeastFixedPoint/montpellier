package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Board;
import info.reflectionsofmind.connexion.core.board.BoardUtil;
import info.reflectionsofmind.connexion.core.board.Feature;
import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.util.Util;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;

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
	
	public static Meeple getFreeMeepleOfType(Board board, Player player, Meeple.Type type)
	{
		for (Meeple meeple : player.getMeeples())
		{
			if (!board.getMeeples().contains(meeple) && meeple.getType() == type)
			{
				return meeple;
			}
		}
		
		return null;
	}

	public static Tile getInitialTile(Game game)
	{
		return game.getBoard().getPlacements().get(0).getTile();
	}
}
