package info.reflectionsofmind.connexion.platform.core.common.message.cts;

import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;

public abstract class AbstractCTSMessage
{
	public static final String EVENT_PREFIX = "connexion:cts-event";
	
	public abstract String encode();
	
	public abstract void dispatch(IClientNode from, ICTSMessageTarget target);
}
