package info.reflectionsofmind.connexion.platform.core.common.message.stc;

public interface ISTCMessageTarget
{
	/** This is called when your connection is accepted. */
	void onConnectionAcceptedMessage(STCMessage_ConnectionAccepted message);
	
	/** This is called when a game or its configuration changes (before start). */
	void onGameChangedMessage(STCMessage_GameChanged message);

	/** This is called when someone connects. */
	void onParticipantConnectedMessage(STCMessage_ParticipantConnected message);

	/** This is called when someone disconnects. */
	void onParticipantDisconnectedMessage(STCMessage_ParticipantDisconnected message);

	/** This is called when someone's state changes. */
	void onParticipantStateChangedMessage(STCMessage_ParticipantStateChanged message);

	/** This is called when the game starts. */
	void onGameStartedMessage(STCMessage_GameStarted message);

	/** This is called when a message arrives. */
	void onChatMessage(STCMessage_Chat message);

	/** This is called when a change has occured in the game. */
	void onChangeMessage(STCMessage_Change message);
}