package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.Form;

/** Server-side transport interface. */
public interface ITransport
{
	Form getForm();

	String getName();
	boolean isServerSideOnly();
	
	void start() throws TransportException;
	void stop() throws TransportException;
	void send(INode to, String message) throws TransportException;
	INode getNode(String id);

	void addListener(IListener listener);
	void removeListener(IListener listener);
	
	public interface IListener
	{
		void onTransportMessage(INode from, String message);
	}
}
