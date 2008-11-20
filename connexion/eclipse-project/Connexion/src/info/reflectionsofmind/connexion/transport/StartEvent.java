package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.tile.Tile;

import java.util.List;

public class StartEvent
{
	private final List<Player> players;
	private final Player clientPlayer;
	private final String gameName;
	private final Tile initialTile;
	private final Tile currentTile;
	private final Integer totalTiles;

	public StartEvent( //
			final String gameName, // 
			final List<Player> players, // 
			final Player clientPlayer, // 
			final Tile initialTile, //
			final Tile currentTile,
			final Integer totalTiles)
	{
		super();
		this.gameName = gameName;
		this.players = players;
		this.clientPlayer = clientPlayer;
		this.initialTile = initialTile;
		this.currentTile = currentTile;
		this.totalTiles = totalTiles;
	}

	public Player getClientPlayer()
	{
		return this.clientPlayer;
	}

	public List<Player> getPlayers()
	{
		return this.players;
	}

	public String getGameName()
	{
		return this.gameName;
	}

	public Tile getInitialTile()
	{
		return this.initialTile;
	}
	
	public Tile getCurrentTile()
	{
		return this.currentTile;
	}
	
	public Integer getTotalNumberOfTiles()
	{
		return this.totalTiles;
	}
}
