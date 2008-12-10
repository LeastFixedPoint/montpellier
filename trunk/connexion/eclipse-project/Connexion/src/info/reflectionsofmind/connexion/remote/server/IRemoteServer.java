package info.reflectionsofmind.connexion.remote.server;

import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.stc.IServerToClientEventListener;

public interface IRemoteServer
{
	void start() throws ServerConnectionException;
	
	void stop() throws ServerConnectionException;
	
	void sendEvent(ClientToServerEvent event) throws ServerConnectionException, RemoteServerException;

	void addListener(IServerToClientEventListener listener);

	void removeListener(IServerToClientEventListener listener);
}
