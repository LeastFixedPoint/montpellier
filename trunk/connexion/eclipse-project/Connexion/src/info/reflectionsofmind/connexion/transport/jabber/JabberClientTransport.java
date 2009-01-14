package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.AbstractClientTransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import org.jivesoftware.smack.XMPPException;

public class JabberClientTransport extends AbstractClientTransport implements JabberCore.IListener
{
	private final JabberCore core;
	private final String serverAddress;

	public JabberClientTransport(String initString, String serverAddress) throws TransportException
	{
		this.serverAddress = serverAddress;
		this.core = new JabberCore(new JabberConnectionInfo(initString));
		this.core.addListener(this);
	}

	@Override
	public void onPacket(String from, String content)
	{
		if (from.equals(this.serverAddress))
		{
			for (IListener listener : getListeners())
			{
				listener.onPacket(content);
			}
		}
	}

	public void send(String contents) throws TransportException
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
}
