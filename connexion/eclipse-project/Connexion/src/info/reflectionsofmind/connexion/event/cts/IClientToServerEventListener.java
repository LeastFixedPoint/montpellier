package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.transport.INode;

public interface IClientToServerEventListener
{
	void onClientConnectionRequestEvent(INode from, ClientToServer_ClientConnectionRequestEvent event);
	void onDisconnectNoticeEvent(INode from, ClientToServer_DisconnectNoticeEvent event);
	void onTurnEvent(INode from, ClientToServer_TurnEvent event);
	void onMessageEvent(INode from, ClientToServer_MessageEvent event);
}
