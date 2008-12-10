package info.reflectionsofmind.connexion.event.stc;


public interface IServerToClientEventListener
{  
	/** This is called when your connection is accepted. */
	void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event);
	
	/** This is called when someone connects. */
	void onClientConnected(ServerToClient_ClientConnectedEvent event);

	/** This is called when someone disconnects. */
	void onClientDisconnected(ServerToClient_ClientDisconnectedEvent event);

	/** This is called when somebody is accepted into game as player. */
	void onPlayerAccepted(ServerToClient_PlayerAcceptedEvent event);

	/** This is called when somebody is accepted into game as spectator. */
	void onSpectatorAccepted(ServerToClient_SpectatorAcceptedEvent serverToClient_SpectatorAcceptedEvent);

	/** This is called when a player is rejected from game. */
	void onPlayerRejected(ServerToClient_PlayerRejectedEvent event);

	/** This is called when a spectator is rejected from game. */
	void onSpectatorRejected(ServerToClient_SpectatorRejectedEvent serverToClient_SpectatorRejectedEvent);

	/** This is called when someone makes a turn. */
	void onTurn(ServerToClient_TurnEvent event);

	/** This is called when the game starts. */
	void onStart(ServerToClient_GameStartedEvent event);

	/** Client calls this when it send a message. */
	void onMessage(ServerToClient_MessageEvent event);

}