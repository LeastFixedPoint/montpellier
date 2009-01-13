package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;

public interface IClientTransportFactory extends INamed
{
	IClientTransport createTransport(String initString) throws TransportException;
}
