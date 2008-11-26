package info.reflectionsofmind.connexion.event.stc;

import info.reflectionsofmind.connexion.util.convert.IConvertible;

public abstract class ServerToClientEvent<T extends ServerToClientEvent<T>> implements IConvertible<T>
{
	public static final String PREFIX = "connexion:stc-event";
}
