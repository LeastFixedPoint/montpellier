package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.platform.client.IClient;
import info.reflectionsofmind.connexion.platform.server.IServer;
import info.reflectionsofmind.connexion.platform.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.platform.transport.IServerTransportFactory;

import java.util.List;

public interface IApplication
{
	IServer newServer();

	IClient newClient();

	List<IServerTransportFactory> getServerTransportFactories();

	List<IClientTransportFactory> getClientTransportFactories();
}
