package info.reflectionsofmind.connexion.platform.transport;


public interface IClientNode
{
	IServerTransport getTransport();
	void send(String contents) throws TransportException;
}