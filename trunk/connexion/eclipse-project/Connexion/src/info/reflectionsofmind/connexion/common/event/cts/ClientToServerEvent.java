package info.reflectionsofmind.connexion.common.event.cts;

import info.reflectionsofmind.connexion.transport.IClientNode;

public abstract class ClientToServerEvent
{
	public static final String EVENT_PREFIX = "connexion:cts-event";
	
	public abstract String encode();
	
	public abstract void dispatch(IClientNode from, IClientToServerEventListener target);
}
