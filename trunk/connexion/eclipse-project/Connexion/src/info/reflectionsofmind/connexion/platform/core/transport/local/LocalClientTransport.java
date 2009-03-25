package info.reflectionsofmind.connexion.platform.core.transport.local;

import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.platform.core.transport.AbstractClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

import javax.swing.SwingUtilities;

public class LocalClientTransport extends AbstractClientTransport
{
	private final LocalClientNode node;
	private final int index;
	private boolean running = false;

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
		System.out.format("[%s] => [%s]: %s\n", "Server", getName(), contents);
		if (this.running) firePacket(contents);
	}

	public void display()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final JoinGameFrame joinGameFrame = new JoinGameFrame(getNode().getTransport().getApplication().newClient());
				joinGameFrame.setVisible(true);
				joinGameFrame.connect(LocalClientTransport.this);
			}
		});
	}

	@Override
	public void start() throws TransportException
	{
		this.running = true;
	}

	@Override
	public void stop()
	{
		this.running = false;
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
