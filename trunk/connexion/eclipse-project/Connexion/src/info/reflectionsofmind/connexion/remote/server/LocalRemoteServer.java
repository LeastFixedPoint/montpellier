package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport;
import info.reflectionsofmind.connexion.transport.local.AbstractLocalTransport.AbstractLocalNode;
import info.reflectionsofmind.connexion.transport.local.ServerLocalTransport.ServerLocalNode;

public class LocalRemoteServer extends RemoteServer<ClientLocalTransport, AbstractLocalNode>
{
	public LocalRemoteServer(ClientLocalTransport transport, ServerLocalNode serverNode)
	{
		super(transport, serverNode);
	}
}
