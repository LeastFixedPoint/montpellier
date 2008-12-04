package info.reflectionsofmind.connexion.transport;


public interface ITransport<SenderType extends INode>
{
	void start() throws TransportException;
	void stop() throws TransportException;
	void send(SenderType to, String message) throws TransportException;
	
	void addListener(IListener<SenderType> listener);
	void removeListener(IListener<SenderType> listener);
	String getName();
	
	public interface IListener<SenderType extends INode>
	{
		void onMessage(SenderType from, String message);
	}
}
