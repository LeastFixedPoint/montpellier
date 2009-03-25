package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public final class TilePlacementAction extends AbstractAction
{
	private final Tile tile;
	private final ILocation location;
	private final IDirection direction;

	public TilePlacementAction(final Player player, final Tile tile, final ILocation location, final IDirection direction)
	{
		super(player);

		this.tile = tile;
		this.location = location;
		this.direction = direction;
	}

	@Override
	public void dispatch(final ServerGame serverFortress)
	{
		serverFortress.onTilePlacement(this);
	}

	public IDirection getDirection()
	{
		return this.direction;
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public Tile getTile()
	{
		return this.tile;
	}

	public static final class Coder extends AbstractCoder<TilePlacementAction>
	{
		private final ClientGame game;

		public Coder(final ClientGame game)
		{
			this.game = game;
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("place-tile#");
		}

		@Override
		public TilePlacementAction decode(final String string)
		{
			final String[] strings = string.split("#");

			final Integer playerIndex = Util.strToInt(strings[1]);
			final Player player = playerIndex == null ? null : this.game.getPlayers().get(playerIndex);

			final Tile tile;
			try
			{
				tile = new Tile(strings[2]);
			}
			catch (final TileCodeFormatException exception)
			{
				throw new RuntimeException(exception);
			}

			final RectangularGeometry geometry = (RectangularGeometry) this.game.getBoard().getGeometry();
			final ILocation location = geometry.newLocation(Util.strToInt(strings[3]), Util.strToInt(strings[4]));
			final IDirection direction = geometry.newDirection(Util.strToInt(strings[5]));

			return new TilePlacementAction(player, tile, location, direction);
		}

		@Override
		public String encode(final TilePlacementAction action)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("place-tile");
			builder.append("#").append(this.game.getPlayers().indexOf(action.getPlayer()));
			builder.append("#").append(action.getTile().getCode());
			builder.append("#").append(((Location) action.getLocation()).getX());
			builder.append("#").append(((Location) action.getLocation()).getY());
			builder.append("#").append(action.getDirection().getIndex());

			return builder.toString();
		}
	}
}
