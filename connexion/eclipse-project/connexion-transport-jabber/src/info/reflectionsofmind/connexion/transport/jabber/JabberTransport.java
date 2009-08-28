package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.TransportNode;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class JabberTransport extends AbstractTransport implements PacketListener
{
	private XMPPConnection connection;
	private final String host, node, password, resource;
	private final Integer port;
	
	public JabberTransport(final String node, final String host, final Integer port, final String password,
			final String resource)
	{
		this.node = node;
		this.host = host;
		this.port = port;
		this.password = password;
		this.resource = resource;
	}
	
	@Override
	public String getName()
	{
		return "Jabber: " + this.node + ":" + this.password + "@" + this.host + ":" + this.port + "/" + this.resource;
	}
	
	@Override
	public void start()
	{
		try
		{
			SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			
			final ConnectionConfiguration configuration = new ConnectionConfiguration(getHost(), getPort());
			
			this.connection = new XMPPConnection(configuration);
			this.connection.connect();
			
			if (getResource() != null)
				this.connection.login(getNode(), getPassword(), getResource());
			else
				this.connection.login(getNode(), getPassword());
			
			this.connection.addPacketListener(this, null);
			
			fireStarted();
		}
		catch (final XMPPException exception)
		{
			fireError(exception.getMessage());
			exception.printStackTrace();
		}
	}
	
	@Override
	public void stop()
	{
		this.connection.disconnect();
		this.connection = null;
		fireStopped();
	}
	
	@Override
	public void send(final TransportNode recipient, final String contents)
	{
		final Message message = new Message(recipient.getAddress());
		message.setBody(contents);
		this.connection.sendPacket(message);
	}
	
	@Override
	public void processPacket(final Packet packet)
	{
		if (!(packet instanceof Message)) return;
		firePacket(new JabberNode(packet.getFrom()), ((Message) packet).getBody());
	}
	
	public String getHost()
	{
		return this.host;
	}
	
	public Integer getPort()
	{
		return this.port;
	}
	
	public String getNode()
	{
		return this.node;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public String getResource()
	{
		return this.resource;
	}
	
	private final class JabberNode extends TransportNode
	{
		public JabberNode(final String jabberAddress)
		{
			super(JabberTransport.this, jabberAddress);
		}
	}
}