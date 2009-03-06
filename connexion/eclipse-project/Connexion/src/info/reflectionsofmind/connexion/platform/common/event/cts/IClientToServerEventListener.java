package info.reflectionsofmind.connexion.platform.common.event.cts;

import info.reflectionsofmind.connexion.platform.transport.IClientNode;

public interface IClientToServerEventListener
{
	void onClientConnectionRequestEvent(IClientNode from, ClientToServer_ClientConnectionRequestEvent event);
	void onDisconnectNoticeEvent(IClientNode from, ClientToServer_DisconnectNoticeEvent event);
	void onTurnEvent(IClientNode from, ClientToServer_TurnEvent event);
	void onMessageEvent(IClientNode from, ClientToServer_ChatMessageEvent event);
}
