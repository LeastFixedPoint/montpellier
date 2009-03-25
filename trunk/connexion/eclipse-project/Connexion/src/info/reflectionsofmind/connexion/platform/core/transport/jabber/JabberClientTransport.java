package info.reflectionsofmind.connexion.platform.core.transport.jabber;

import info.reflectionsofmind.connexion.platform.core.transport.AbstractClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;

import org.jivesoftware.smack.XMPPException;

public class JabberClientTransport extends AbstractClientTransport implements JabberCore.IListener
{
	private final JabberCore core;
	private final String serverAddress;

	public JabberClientTransport(final String initString, final String serverAddress) throws TransportException
	{
		this.serverAddress = serverAddress;
		this.core = new JabberCore(new JabberConnectionInfo(initString));
		this.core.addListener(this);
	}

	@Override
	public void onPacket(final String from, final String content)
	{
		if (from.equals(this.serverAddress))
		{
			for (final IListener listener : getListeners())
			{
				listener.onPacket(content);
			}
		}
	}

	public void send(final String contents) throws TransportException
	{
		this.core.send(this.serverAddress, contents);
	}

	@Override
	public void start() throws TransportException
	{
		try
		{
			this.core.start();
		}
		catch (final XMPPException exception)
		{
			throw new TransportException(exception);
		}
	}

	@Override
	public void stop()
	{
		this.core.stop();
	}
	
	@Override
	public String getName()
	{
		return "Jabber";
	}
}
