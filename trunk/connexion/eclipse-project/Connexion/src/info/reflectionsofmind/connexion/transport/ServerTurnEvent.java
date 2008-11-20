package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.OrientedTile;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.core.tile.Tile;

public class ServerTurnEvent
{
	private final Player player;

	private final OrientedTile tile;
	private final ILocation location;

	private final Meeple meeple;
	private final Section section;
	
	private final Player currentPlayer;
	private final Tile currentTile;

	public ServerTurnEvent( //
			final Player player, //
			final OrientedTile tile, //
			final ILocation location, //
			final Meeple meeple, //
			final Section section, //
			final Player currentPlayer, //
			final Tile currentTile)
	{
		this.player = player;
		this.tile = tile;
		this.location = location;
		this.meeple = meeple;
		this.section = section;
		this.currentPlayer = currentPlayer;
		this.currentTile = currentTile;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Player getCurrentPlayer()
	{
		return this.currentPlayer;
	}
	
	public Tile getCurrentTile()
	{
		return this.currentTile;
	}

	public OrientedTile getOrientedTile()
	{
		return this.tile;
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}

}
