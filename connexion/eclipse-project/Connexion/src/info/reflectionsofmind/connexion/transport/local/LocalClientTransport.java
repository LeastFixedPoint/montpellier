package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.transport.AbstractClientTransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import javax.swing.SwingUtilities;

public class LocalClientTransport extends AbstractClientTransport
{
	private final LocalClientNode node;
	private final int index;

	public LocalClientTransport(final LocalServerTransport serverTransport, final int index)
	{
		this.node = new LocalClientNode(serverTransport, this);
		this.index = index;
	}

	@Override
	public void send(String contents) throws TransportException
	{
		this.node.getTransport().receive(getNode(), contents);
	}

	public void receive(String contents)
	{
		firePacket(contents);
	}

	@Override
	public void start() throws TransportException
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final JoinGameFrame joinGameFrame = new JoinGameFrame(getNode().getTransport().getApplication().newClient());
				joinGameFrame.setVisible(true);
				joinGameFrame.getClient().connect(LocalClientTransport.this);
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

	public String getClientName()
	{
		return "Local Player #" + this.index;
	}
	
	@Override
	public String getName()
	{
		return "Local #" + this.index;
	}
}
