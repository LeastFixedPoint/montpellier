package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.remote.server.IRemoteServer;

public abstract class ServerToClientEvent
{
	public static final String EVENT_PREFIX = "connexion:stc-event";
	
	public abstract String encode();

	public abstract void dispatch(IRemoteServer.IListener listener);
}
