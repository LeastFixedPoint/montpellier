package info.reflectionsofmind.connexion.remote.client;

import java.util.List;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent.Reason;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;

public interface IRemoteClient
{
	/** Call this to notify that its connection is accepted. */
	void acceptConnection(List<Player> existingPlayers);
	
	/** Call this to notify that it is disconnected. */
	void disconnect(DisconnectReason reason) throws ClientConnectionException;
	
	/** Call this to notify client about a game start. */
	void sendStart(Game game) throws ClientConnectionException;
	
	/** Call this to notify client about a turn. */
	void sendTurn(Turn turn, Tile newCurrentTile) throws ClientConnectionException;
	
	/** Call this to route a message to client. */
	void sendMessage(int playerIndex, String message) throws ClientConnectionException;
	
	/** Call this to tell client someone has connected. */
	void sendPlayerConnected(Player player);
	
	/** Call this to tell client someone has disconnected. */
	void sendPlayerDisconnected(int playerIndex, Reason reason);
	
	String getName();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		/** Client calls this when it wants to connect. */
		void onConnectionRequest(IRemoteClient sender, ClientToServer_ClientConnectionRequestEvent event);
		
		/** Client calls this before it disconnects. */
		void onDisconnect(IRemoteClient sender, ClientToServer_ClientDisconnectedEvent event);
		
		/** Client calls this when it makes a turn. */
		void onTurn(IRemoteClient sender, ClientToServer_TurnEvent event);
		
		/** Client calls this when it send a message. */
		void onMessage(IRemoteClient sender, ClientToServer_MessageEvent event);
	}
}
