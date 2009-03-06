package info.reflectionsofmind.connexion.platform.transport;

import info.reflectionsofmind.connexion.util.INamed;


public interface IClientTransport extends INamed
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
