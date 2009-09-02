package info.reflecitonsofmind.connexion.platform.gui.event.stc;

import info.reflectionsofmind.connexion.transport.TransportNode;

public interface IServerToClientMessageDispatchTarget
{
	void onDisconnectNotice(KickNotice disconnectNotice, TransportNode sender);
	
	void onParticipationAccepted(ParticipationAccepted participationAccepted, TransportNode sender);
}
