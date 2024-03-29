package info.reflectionsofmind.connexion.platform.core.transport.jabber;

import info.reflectionsofmind.connexion.platform.core.transport.AbstractServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.DefaultClientPacket;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.core.transport.IClientPacket;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

import org.jivesoftware.smack.XMPPException;

public class JabberServerTransport extends AbstractServerTransport implements JabberCore.IListener
{
	private final JabberCore core;

	public JabberServerTransport(String initString) throws TransportException
	{
		this.core = new JabberCore(new JabberConnectionInfo(initString));
		this.core.addListener(this);
	}
	
	@Override
	public String getName()
	{
		return "Jabber";
	}

	@Override
	public void onPacket(String from, String content)
	{
		final JabberClientNode node = new JabberClientNode(from);
		final IClientPacket packet = new DefaultClientPacket(node, content);

		for (IListener listener : getListeners())
		{
			listener.onPacket(packet);
		}
	}

	public void send(JabberClientNode to, String contents) throws TransportException
	{
		this.core.send(to.getAddress(), contents);
	}

	@Override
	public void start() throws TransportException
	{
		try
		{
			this.core.start();
		}
		catch (XMPPException exception)
		{
			throw new TransportException(exception);
		}
	}

	@Override
	public void stop()
	{
		this.core.stop();
	}

	public class JabberClientNode implements IClientNode
	{
		private final String address;

		public JabberClientNode(String address)
		{
			this.address = address;
		}

		public String getAddress()
		{
			return this.address;
		}

		public JabberServerTransport getTransport()
		{
			return JabberServerTransport.this;
		}

		@Override
		public void send(String contents) throws TransportException
		{
			JabberServerTransport.this.send(this, contents);
		}
	}
}
