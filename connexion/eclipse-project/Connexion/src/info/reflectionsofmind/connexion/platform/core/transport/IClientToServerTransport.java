package info.reflectionsofmind.connexion.platform.core.transport;

import info.reflectionsofmind.connexion.util.INamed;

public interface IClientToServerTransport extends INamed
{
	void start() throws TransportException;
	void stop();
	void send(String contents) throws TransportException;

	void addListener(IListener listener);
	void removeListener(IListener listener);

	public interface IListener
	{
		void onStarted();
		void onPacket(String contents);
		void onError(TransportException exception);
		void onStopped();
	}
}
