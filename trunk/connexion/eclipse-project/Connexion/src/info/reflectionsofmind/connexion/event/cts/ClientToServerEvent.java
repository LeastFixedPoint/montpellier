package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.transport.INode;


public abstract class ClientToServerEvent
{
	public static final String EVENT_PREFIX = "connexion:cts-event";
	
	public abstract String encode();
	
	public abstract void dispatch(INode origin, IClientToServerEventTarget target);
}
