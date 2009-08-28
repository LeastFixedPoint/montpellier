package info.reflecitonsofmind.connexion.platform.gui.event.cts;

import info.reflectionsofmind.connexion.transport.TransportNode;

public interface IClientToServerMessage
{
	void dispatch(IClientToServerMessageDispatchTarget target, TransportNode sender);
}
