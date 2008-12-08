package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.remote.client.IRemoteClient;


public abstract class ClientToServerEvent
{
	public static final String EVENT_PREFIX = "connexion:cts-event";
	
	public abstract String encode();
	
	public abstract void dispatch(IRemoteClient sender, IRemoteClient.IListener listener);
}
