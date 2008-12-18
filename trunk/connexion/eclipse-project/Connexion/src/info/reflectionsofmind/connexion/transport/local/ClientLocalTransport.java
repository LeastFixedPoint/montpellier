package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.Form;

public class ClientLocalTransport extends AbstractLocalTransport
{
	private final ServerLocalTransport serverNode;
	private final Settings settings;
	
	public ClientLocalTransport(ServerLocalTransport serverNode, final Settings settings)
	{
		this.serverNode = serverNode;
		this.settings = settings;

		final JoinGameFrame joinGameFrame = new JoinGameFrame(this.settings);
		joinGameFrame.setVisible(true);
		joinGameFrame.connect(getServerNode());
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
		throw new UnsupportedOperationException();
	}
}
