package info.reflectionsofmind.connexion.platform.core.transport;

public interface IServerToClientTransport
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
		void onBeforeStopped(IServerToClientTransport transport);
	}
}
