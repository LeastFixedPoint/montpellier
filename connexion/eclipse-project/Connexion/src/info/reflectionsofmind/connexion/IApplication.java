package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.platform.core.client.IClient;
import info.reflectionsofmind.connexion.platform.core.common.IUserInterface;
import info.reflectionsofmind.connexion.platform.core.server.IServer;
import info.reflectionsofmind.connexion.platform.core.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;

import java.util.List;

public interface IApplication
{
	IUserInterface getUI();
	
	IServer newServer();

	IClient newClient();

	List<IServerTransportFactory> getServerTransportFactories();

	List<IClientTransportFactory> getClientTransportFactories();
}
