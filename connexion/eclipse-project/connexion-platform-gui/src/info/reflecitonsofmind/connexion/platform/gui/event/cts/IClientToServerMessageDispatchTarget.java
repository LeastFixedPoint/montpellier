package info.reflecitonsofmind.connexion.platform.gui.event.cts;

import info.reflectionsofmind.connexion.transport.TransportNode;

public interface IClientToServerMessageDispatchTarget
{
	void onDisconnectNotice(DisconnectNotice disconnectNotice, TransportNode sender);
	
	void onParticipationRequest(ParticipationRequest participationRequest, TransportNode sender);
}
