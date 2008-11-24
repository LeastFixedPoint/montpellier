package info.reflectionsofmind.connexion.server.remote;

import info.reflectionsofmind.connexion.client.remote.IRemoteServer;
import info.reflectionsofmind.connexion.server.local.DisconnectReason;
import info.reflectionsofmind.connexion.transport.ServerTurnEvent;
import info.reflectionsofmind.connexion.transport.StartEvent;

public class JabberRemoteClient extends AbstractRemoteClient implements IRemoteServer.IListener
{
	@Override
	public void sendStart(StartEvent start) throws ClientConnectionException
	{
	}

	@Override
	public void sendTurn(ServerTurnEvent turn) throws ClientConnectionException
	{
	}

	@Override
	public void onStart(StartEvent event)
	{
	}

	@Override
	public void onTurn(ServerTurnEvent event)
	{
	}

	@Override
	public void disconnect(DisconnectReason reason) throws ClientConnectionException
	{
	}

	@Override
	public String getName()
	{
		return null;
	}
}
