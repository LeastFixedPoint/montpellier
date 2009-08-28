package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;

public interface ITransport extends INamed
{
	void start();
	
	void send(TransportNode recipient, String contents);
	
	void stop();
	
	void addListener(IListener listener);
	
	public interface IListener
	{
		void onStarted(ITransport transport);
		
		void onPacket(TransportNode sender, String contents);
		
		void onError(ITransport transport, String error);
		
		void onStopped(ITransport transport);
	}
}
