package info.reflectionsofmind.connexion.platform.core.common.message.stc;


public abstract class AbstractSTCMessage
{
	public static final String EVENT_PREFIX = "connexion:stc-event";
	
	public abstract String encode();

	public abstract void dispatch(ISTCMessageTarget listener);
}
