package info.reflecitonsofmind.connexion.platform.gui.event.cts;

import info.reflectionsofmind.connexion.transport.TransportNode;

public class DisconnectNotice implements IClientToServerMessage
{
	@Override
	public void dispatch(final IClientToServerMessageDispatchTarget target, final TransportNode sender)
	{
		target.onDisconnectNotice(this, sender);
	}
}
