package info.reflectionsofmind.connexion.transport;

public interface ITransport
{
	void start() throws TransportException;
	void stop() throws TransportException;
	void send(INode to, String message) throws TransportException;
	
	void addListener(IListener listener);
	void removeListener(IListener listener);
	String getName();
	
	public interface IListener
	{
		void onTransportMessage(INode from, String message);
	}
}
