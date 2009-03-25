package info.reflectionsofmind.connexion.platform.core.transport;


public interface IClientNode
{
	IServerTransport getTransport();
	void send(String contents) throws TransportException;
}