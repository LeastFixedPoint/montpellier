package info.reflectionsofmind.connexion.remote.client;

import java.util.List;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent.Reason;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;

public class JabberRemoteClient extends AbstractRemoteClient
{
	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	@Override
	public void acceptConnection(List<Player> existingPlayers)
	{
	}
	
	@Override
	public void sendStart(Game game) throws ClientConnectionException
	{
	}
	
	@Override
	public void sendTurn(Turn turn, Tile newCurrentTile) throws ClientConnectionException
	{
	}
	
	@Override
	public void sendPlayerConnected(Player player)
	{
	}
	
	@Override
	public void sendPlayerDisconnected(int playerIndex, Reason reason)
	{
	}

	@Override
	public void disconnect(DisconnectReason reason) throws ClientConnectionException
	{
	}

	// ============================================================================================
	// === GETTERS AND SETTERS
	// ============================================================================================

	@Override
	public String getName()
	{
		return null;
	}
}
