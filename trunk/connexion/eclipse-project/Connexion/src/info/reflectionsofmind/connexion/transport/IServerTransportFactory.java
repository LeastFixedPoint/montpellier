package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;

public interface IServerTransportFactory extends INamed
{
	IServerTransport createTransport(String initString) throws TransportException;
}
