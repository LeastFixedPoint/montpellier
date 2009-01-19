package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.client.IClient;
import info.reflectionsofmind.connexion.server.IServer;
import info.reflectionsofmind.connexion.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.transport.IServerTransportFactory;

import java.util.List;

public interface IApplication
{
	IServer newServer();

	IClient newClient();

	List<IServerTransportFactory> getServerTransportFactories();

	List<IClientTransportFactory> getClientTransportFactories();
}
