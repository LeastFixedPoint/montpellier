package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.Form;
import info.reflectionsofmind.connexion.util.IComponent;

public interface ITransport extends IComponent
{
	Form getForm();

	String getName();
	boolean isServerSideOnly();
	
	void configure(Form configuration);
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
