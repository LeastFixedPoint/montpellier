package info.reflectionsofmind.connexion.platform.core.common.event.stc;


public abstract class ServerToClientEvent
{
	public static final String EVENT_PREFIX = "connexion:stc-event";
	
	public abstract String encode();

	public abstract void dispatch(IServerToClientEventListener listener);
}
