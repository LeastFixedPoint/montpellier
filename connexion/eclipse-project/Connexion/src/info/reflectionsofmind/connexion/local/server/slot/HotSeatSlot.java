package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.transport.hotseat.HotSeatTransport;

public class HotSeatSlot extends Slot<HotSeatTransport.Addressee, HotSeatTransport>
{
	public HotSeatSlot(IServer server, Class<HotSeatTransport> transportClass)
	{
		super(server, ServerUtil.findTransport(server, HotSeatTransport.class));
	}
}
