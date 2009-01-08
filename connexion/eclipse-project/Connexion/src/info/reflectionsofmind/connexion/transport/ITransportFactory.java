package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.IComponent;

public interface ITransportFactory extends IComponent
{
	ITransport createTransport();
}
