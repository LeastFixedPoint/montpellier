package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.transport.AbstractClientTransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import javax.swing.SwingUtilities;

public class ClientLocalTransport extends AbstractClientTransport
{
	private final LocalClientNode node;
	
	public ClientLocalTransport(final ServerLocalTransport serverTransport, final int index)
	{
		this.node = new LocalClientNode(serverTransport, this);
	}
	
	@Override
	public void send(String contents) throws TransportException
	{
		this.node.getTransport().receive(getNode(), contents);
	}
	
	public void receive(String contents)
	{
		
	}
	
	@Override
	public void start() throws TransportException
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final JoinGameFrame joinGameFrame = new JoinGameFrame(getNode().getTransport().getApplication());
				joinGameFrame.setVisible(true);
				joinGameFrame.getClient().connect(ClientLocalTransport.this);
			}
		});
	}
	
	@Override
	public void stop()
	{
	}
	
	public LocalClientNode getNode()
	{
		return this.node;
	}
}
