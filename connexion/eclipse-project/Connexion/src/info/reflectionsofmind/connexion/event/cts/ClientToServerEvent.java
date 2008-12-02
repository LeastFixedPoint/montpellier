package info.reflectionsofmind.connexion.event.cts;


public abstract class ClientToServerEvent
{
	public static final String EVENT_PREFIX = "connexion:cts-event";
	
	public abstract String encode();
}
