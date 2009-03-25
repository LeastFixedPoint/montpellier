package info.reflectionsofmind.connexion.platform.core.transport;

public interface IServerTransport
{
	void start() throws TransportException;
	void stop();

	String getName();

	void addListener(IListener listener);
	void removeListener(IListener listener);

	public interface IListener
	{
		void onPacket(IClientPacket packet);
		void onError(TransportException exception);
		void onBeforeStopped(IServerTransport transport);
	}
}
