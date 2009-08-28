package info.reflecitonsofmind.connexion.platform.gui.event.stc;

import info.reflectionsofmind.connexion.transport.TransportNode;

public interface IServerToClientMessage
{
	void dispatch(IServerToClientMessageDispatchTarget target, TransportNode sender);
}
