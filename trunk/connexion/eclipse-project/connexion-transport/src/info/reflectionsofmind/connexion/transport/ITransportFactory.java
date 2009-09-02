package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;

public interface ITransportFactory extends INamed
{
	ITransport createTransport(IConnectionParameters parameters);
	
	IConnectionParameters fromString(String parameters);
}
