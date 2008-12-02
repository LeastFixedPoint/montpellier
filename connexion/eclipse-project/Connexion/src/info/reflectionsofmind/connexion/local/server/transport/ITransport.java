package info.reflectionsofmind.connexion.local.server.transport;

public interface ITransport
{
	void start() throws TransportException;
	void stop() throws TransportException;
	void send(ISender sender, String message) throws TransportException;
	
	void addListener(IListener listener);
	String getName();
	
	public interface IListener
	{
		void onMessage(ISender sender, String message);
	}
}
