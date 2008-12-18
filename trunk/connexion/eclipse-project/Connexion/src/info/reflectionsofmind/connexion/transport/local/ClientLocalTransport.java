package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.remote.server.LocalRemoteServer;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.Form;

public class ClientLocalTransport extends AbstractLocalTransport
{
	private final ServerLocalTransport serverNode;
	
	public ClientLocalTransport(ServerLocalTransport serverNode)
	{
		this.serverNode = serverNode;
	}

	@Override
	public Form getForm()
	{
		return null;
	}
	
	public ServerLocalTransport getServerNode()
	{
		return this.serverNode;
	}
	
	@Override
	public void start() throws TransportException
	{
		new JoinGameFrame(null, new LocalRemoteServer(this)).setVisible(true);
	}
}
