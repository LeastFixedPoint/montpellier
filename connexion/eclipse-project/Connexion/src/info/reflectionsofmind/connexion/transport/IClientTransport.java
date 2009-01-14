package info.reflectionsofmind.connexion.transport;


public interface IClientTransport
{
	void start() throws TransportException;
	void stop();
	void send(String contents) throws TransportException;

	void addListener(IListener listener);
	void removeListener(IListener listener);

	public interface IListener
	{
		void onPacket(String contents);
		void onError(TransportException exception);
	}
}
