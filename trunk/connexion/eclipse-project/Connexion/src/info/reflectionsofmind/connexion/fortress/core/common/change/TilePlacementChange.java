package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;

public final class TilePlacementChange extends AbstractChange
{
	private final Player player;
	private final Tile tile;
	private final ILocation location;
	private final IDirection direction;

	private TilePlacementChange(Player player, Tile tile, ILocation location, IDirection direction)
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
	public void dispatch(ClientGame game)
	{
		game.onTilePlaced(this);
	}
}
