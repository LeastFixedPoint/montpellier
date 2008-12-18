package info.reflectionsofmind.connexion.event.stc;


public interface IServerToClientEventListener
{  
	/** This is called when your connection is accepted. */
	void onConnectionAccepted(ServerToClient_ConnectionAcceptedEvent event);
	
	/** This is called when someone connects. */
	void onClientConnected(ServerToClient_ClientConnectedEvent event);

	/** This is called when someone disconnects. */
	void onClientDisconnected(ServerToClient_ClientDisconnectedEvent event);

	/** This is called when someone's state changes. */
	void onClientStateChanged(ServerToClient_ClientStateChangedEvent event);

	/** This is called when someone makes a turn. */
	void onTurn(ServerToClient_TurnEvent event);

	/** This is called when the game starts. */
	void onStart(ServerToClient_GameStartedEvent event);

	/** Client calls this when it send a message. */
	void onChatMessage(ServerToClient_ChatMessageEvent event);

}