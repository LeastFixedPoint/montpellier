package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;

public class ServerTurnEvent
{
	private final Turn turn;
	private final Tile currentTile;

	public ServerTurnEvent(Turn turn, final Tile currentTile)
	{
		this.turn = turn;
		this.currentTile = currentTile;
	}

	public Turn getTurn()
	{
		return this.turn;
	}
	
	public Tile getCurrentTile()
	{
		return this.currentTile;
	}
}
