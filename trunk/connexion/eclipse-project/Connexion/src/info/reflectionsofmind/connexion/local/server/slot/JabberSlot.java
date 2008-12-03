package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;

public class JabberSlot extends Slot<JabberTransport.Addressee, JabberTransport>
{
	public JabberSlot(IServer server)
	{
		super(server, ServerUtil.findTransport(server, JabberTransport.class));
	}

	@Override
	public void onMessage(JabberTransport.Addressee from, String message)
	{
		
	}
}
