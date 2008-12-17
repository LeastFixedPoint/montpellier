package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

public final class RemoteClient implements IRemoteClient
{
	private final Client client;
	private final INode node;

	public RemoteClient(final Client client, final INode clientNode)
	{
		this.client = client;
		this.node = clientNode;
	}

	@Override
	public void sendEvent(final ServerToClientEvent event) throws TransportException
	{
		getNode().getTransport().send(this.node, event.encode());
	}
	
	@Override
	public void sendChatMessage(IServer server, IRemoteClient client, String message)
	{
		final int index = server.getClients().indexOf(client);
		getNode().getTransport().send(getNode(), new ServerToClient_MessageEvent(index, message).);
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	@Override
	public Client getClient()
	{
		return this.client;
	}

	@Override
	public INode getNode()
	{
		return this.node;
	}
}
