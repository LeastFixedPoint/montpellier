package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.BoardUtil;
import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Section;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.AbstractClientDecoder;
import info.reflectionsofmind.connexion.platform.core.server.AbstractServerEncoder;
import info.reflectionsofmind.connexion.util.Util;

public final class MeeplePlacementChange extends AbstractChange
{
	private final Player player;
	private final Meeple meeple;
	private final Section section;

	private MeeplePlacementChange(Player player, Meeple meeple, Section section)
	{
		this.player = player;
		this.meeple = meeple;
		this.section = section;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}

	@Override
	public void dispatch(ClientGame game)
	{
		game.onMeeplePlaced(this);
	}

	public static final class Encoder extends AbstractServerEncoder<ServerGame, MeeplePlacementChange>
	{
		public Encoder(final ServerGame game)
		{
			super(game);
		}

		@Override
		public String encode(final MeeplePlacementChange change)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("meeple-placed");
			builder.append("#").append(getGame().getPlayers().indexOf(change.getPlayer()));
			builder.append("#").append(change.getPlayer().getMeeples().indexOf(change.getMeeple()));

			final Tile tile = change.getSection().getTile();
			final ILocation location = BoardUtil.getPlacementOf(getGame().getBoard(), tile).getLocation();

			builder.append("#").append(((Location) location).getX());
			builder.append("#").append(((Location) location).getY());
			builder.append("#").append(tile.getSections().indexOf(change.getSection()));

			return builder.toString();
		}
	}

	public static final class Decoder extends AbstractClientDecoder<ClientGame, MeeplePlacementChange>
	{
		public Decoder(final ClientGame game)
		{
			super(game);
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("meeple-placed#");
		}

		@Override
		public MeeplePlacementChange decode(final String string)
		{
			final String[] strings = string.split("#");

			final Integer playerIndex = Util.strToInt(strings[1]);

			final Player player = playerIndex == null ? null : getGame().getPlayers().get(playerIndex);
			final Meeple meeple = player.getMeeples().get(Util.strToInt(strings[2]));

			final RectangularGeometry geometry = (RectangularGeometry) getGame().getBoard().getGeometry();
			final Location location = geometry.newLocation(Util.strToInt(strings[3]), Util.strToInt(strings[4]));

			final Tile tile = BoardUtil.getPlacementAt(getGame().getBoard(), location).getTile();
			final Section section = tile.getSections().get(Util.strToInt(strings[5]));

			return new MeeplePlacementChange(player, meeple, section);
		}
	}
}
