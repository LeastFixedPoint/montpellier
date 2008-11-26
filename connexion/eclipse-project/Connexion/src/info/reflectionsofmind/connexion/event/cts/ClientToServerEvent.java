package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.util.convert.IConvertible;

public abstract class ClientToServerEvent<T extends ClientToServerEvent<T>> implements IConvertible<T>
{
	public static final String PREFIX = "connexion:cts-event";
}
