package info.reflectionsofmind.connexion.transport;

public interface INode
{
	ITransport getTransport();
	void send(String message) throws TransportException;
	String getId(); 
}
