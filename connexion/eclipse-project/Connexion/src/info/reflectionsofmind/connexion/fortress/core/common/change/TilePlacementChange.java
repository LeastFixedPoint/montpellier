package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.AbstractClientDecoder;
import info.reflectionsofmind.connexion.platform.core.server.AbstractServerEncoder;
import info.reflectionsofmind.connexion.util.Util;

public final class TilePlacementChange extends AbstractChange
{
	private final Player player;
	private final Tile tile;
	private final ILocation location;
	private final IDirection direction;

	private TilePlacementChange(final Player player, final Tile tile, final ILocation location, final IDirection direction)
	{
		this.player = player;
		this.tile = tile;
		this.location = location;
		this.direction = direction;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Tile getTile()
	{
		return this.tile;
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public IDirection getDirection()
	{
		return this.direction;
	}

	@Override
	public void dispatch(final ClientGame game)
	{
		game.onTilePlaced(this);
	}

	public static final class Encoder extends AbstractServerEncoder<ServerGame, TilePlacementChange>
	{
		public Encoder(final ServerGame game)
		{
			super(game);
		}

		@Override
		public String encode(final TilePlacementChange change)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("tile-placed");
			builder.append("#").append(getGame().getPlayers().indexOf(change.getPlayer()));
			builder.append("#").append(change.getTile().getCode());
			builder.append("#").append(((Location) change.getLocation()).getX());
			builder.append("#").append(((Location) change.getLocation()).getY());
			builder.append("#").append(change.getDirection().getIndex());

			return builder.toString();
		}
	}

	public static final class Decoder extends AbstractClientDecoder<ClientGame, TilePlacementChange>
	{
		public Decoder(final ClientGame game)
		{
			super(game);
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("tile-placed#");
		}

		@Override
		public TilePlacementChange decode(final String string)
		{
			final String[] strings = string.split("#");

			final Integer playerIndex = Util.strToInt(strings[1]);
			final Player player = playerIndex == null ? null : getGame().getPlayers().get(playerIndex);

			final Tile tile;
			try
			{
				tile = new Tile(strings[2]);
			}
			catch (final TileCodeFormatException exception)
			{
				throw new RuntimeException(exception);
			}

			final RectangularGeometry geometry = (RectangularGeometry) getGame().getBoard().getGeometry();
			final ILocation location = geometry.newLocation(Util.strToInt(strings[3]), Util.strToInt(strings[4]));
			final IDirection direction = geometry.newDirection(Util.strToInt(strings[5]));

			return new TilePlacementChange(player, tile, location, direction);
		}
	}
}
