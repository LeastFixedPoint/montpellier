package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.transport.local.AbstractLocalTransport;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport;

public class LocalRemoteServer extends RemoteServer<ClientLocalTransport, AbstractLocalTransport>
{
	public LocalRemoteServer(ClientLocalTransport transport)
	{
		super(transport, transport.getServerNode());
	}
}
