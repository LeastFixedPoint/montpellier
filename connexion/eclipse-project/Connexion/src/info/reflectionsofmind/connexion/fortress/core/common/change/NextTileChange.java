package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;

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
}
