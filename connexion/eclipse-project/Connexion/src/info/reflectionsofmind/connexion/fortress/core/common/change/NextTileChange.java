package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.platform.core.client.AbstractClientDecoder;
import info.reflectionsofmind.connexion.platform.core.server.AbstractServerEncoder;

public final class NextTileChange extends AbstractChange
{
	private final Tile nextTile;

	private NextTileChange(Tile nextTile)
	{
		this.nextTile = nextTile;
	}

	public Tile getNextTile()
	{
		return this.nextTile;
	}
	
	@Override
	public void dispatch(ClientGame game)
	{
		game.onNextTileChanged(this);
	}

	public static final class Encoder extends AbstractServerEncoder<ServerGame, NextTileChange>
	{
		public Encoder(final ServerGame game)
		{
			super(game);
		}

		@Override
		public String encode(final NextTileChange change)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("tile-changed");
			builder.append("#").append(change.getNextTile().getCode());

			return builder.toString();
		}
	}

	public static final class Decoder extends AbstractClientDecoder<ClientGame, NextTileChange>
	{
		public Decoder(final ClientGame game)
		{
			super(game);
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("tile-changed#");
		}

		@Override
		public NextTileChange decode(final String string)
		{
			final String[] strings = string.split("#");

			try
			{
				return new NextTileChange(new Tile(strings[1]));
			}
			catch (TileCodeFormatException exception)
			{
				throw new RuntimeException(exception);
			}
		}
	}
}
