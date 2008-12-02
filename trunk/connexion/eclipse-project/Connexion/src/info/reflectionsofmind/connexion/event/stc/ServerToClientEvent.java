package info.reflectionsofmind.connexion.event.stc;

public abstract class ServerToClientEvent
{
	public static final String EVENT_PREFIX = "connexion:stc-event";
	
	public abstract String encode();
}
