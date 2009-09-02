package info.reflecitonsofmind.connexion.platform.gui.event.stc;

import info.reflectionsofmind.connexion.transport.TransportNode;

public class ParticipationAccepted implements IServerToClientMessage
{
	public void dispatch(final IServerToClientMessageDispatchTarget target, final TransportNode sender)
	{
		target.onParticipationAccepted(this, sender);
	}
}
