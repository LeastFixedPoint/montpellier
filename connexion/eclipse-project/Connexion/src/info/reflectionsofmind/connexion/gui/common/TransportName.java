package info.reflectionsofmind.connexion.gui.common;

import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport;
import info.reflectionsofmind.connexion.transport.local.ServerLocalTransport;

public enum TransportName
{
	JABBER("Jabber", JabberTransport.class),//
	LOCAL_CLIENT("Local", ClientLocalTransport.class),//
	LOCAL_SERVER("Local", ServerLocalTransport.class);
	
	public final String name;
	public final Class<? extends ITransport> type;
	
	private TransportName(String name, Class<? extends ITransport> type)
	{
		this.name = name;
		this.type = type;
	}

	public static String getName(ITransport transport)
	{
		for (TransportName transportName : TransportName.values())
		{
			if (transportName.type == transport.getClass())
			{
				return transportName.name;
			}
		}
		
		return transport.getClass().getSimpleName();
	}
}
