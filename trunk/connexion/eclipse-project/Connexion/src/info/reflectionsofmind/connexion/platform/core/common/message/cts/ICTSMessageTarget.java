package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;

public interface ICTSMessageTarget
{
	void onConnectionRequest(IClientNode from, CTSMessage_ConnectionRequest event);
	void onDisconnectNotice(IClientNode from, CTSMessage_DisconnectNotice event);
	void onChatMessage(IClientNode from, CTSMessage_Chat event);
	void onAction(IClientNode from, CTSMessage_Action event);
}
