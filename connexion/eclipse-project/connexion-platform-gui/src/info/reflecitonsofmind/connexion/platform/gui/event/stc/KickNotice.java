package info.reflecitonsofmind.connexion.platform.gui.event.stc;

import info.reflectionsofmind.connexion.transport.TransportNode;

public class KickNotice implements IServerToClientMessage
{
	@Override
	public void dispatch(final IServerToClientMessageDispatchTarget target, final TransportNode sender)
	{
		target.onDisconnectNotice(this, sender);
	}
}
