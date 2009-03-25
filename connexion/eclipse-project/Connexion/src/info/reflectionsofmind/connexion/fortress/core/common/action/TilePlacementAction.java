package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;

public final class TilePlacementAction extends AbstractAction
{
	private final ILocation location;
	private final IDirection direction;

	public TilePlacementAction(Player player, ILocation location, IDirection direction)
	{
		super(player);
		
		this.location = location;
		this.direction = direction;
	}
	
	@Override
	public void dispatch(ServerGame serverFortress)
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
}
