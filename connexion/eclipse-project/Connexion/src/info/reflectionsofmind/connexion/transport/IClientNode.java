package info.reflectionsofmind.connexion.transport;


public interface IClientNode
{
	IServerTransport getTransport();
	void send(String contents) throws TransportException;
}