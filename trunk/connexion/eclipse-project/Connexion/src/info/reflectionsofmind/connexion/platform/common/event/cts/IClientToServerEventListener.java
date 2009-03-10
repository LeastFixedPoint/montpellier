package info.reflectionsofmind.connexion.platform.common.event.cts;

import info.reflectionsofmind.connexion.platform.transport.IClientNode;

public interface IClientToServerEventListener
{
	void onConnectionRequest(IClientNode from, ClientToServer_ClientConnectionRequestEvent event);
	void onDisconnectNotice(IClientNode from, ClientToServer_DisconnectNoticeEvent event);
	void onClientTurn(IClientNode from, ClientToServer_TurnEvent event);
	void onChatMessage(IClientNode from, ClientToServer_ChatMessageEvent event);
}
