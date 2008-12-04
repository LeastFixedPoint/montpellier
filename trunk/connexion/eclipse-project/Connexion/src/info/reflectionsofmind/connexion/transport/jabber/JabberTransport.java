package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class JabberTransport extends AbstractTransport<JabberTransport.JabberNode> implements PacketListener
{
	private XMPPConnection connection;

	private JabberAddress address;

	public JabberTransport(final JabberAddress address)
	{
		this.address = address;
	}
	
	@Override
	public void send(final JabberNode to, final String string) throws TransportException
	{
		final Message message = new Message(to.getAddress().asString());
		message.setBody(string);
		this.connection.sendPacket(message);
	}

	@Override
	protected void doFirstStart() throws TransportException
	{
		try
		{
			
			final ConnectionConfiguration configuration = new ConnectionConfiguration( //
					this.address.getHost(), this.address.getPort());
			SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			this.connection = new XMPPConnection(configuration);
			this.connection.connect();
			this.connection.login(this.address.getLogin(), this.address.getPassword());

			this.connection.addPacketListener(this, null);
		}
		catch (final XMPPException exception)
		{
			throw new TransportException(exception);
		}
	}

	@Override
	protected void doFinalStop() throws TransportException
	{
		this.connection.disconnect();
	}

	@Override
	public String getName()
	{
		return "Jabber";
	}

	@Override
	public void processPacket(Packet packet)
	{
		if (!(packet instanceof Message)) return;

		final JabberNode sender = new JabberNode(new JabberAddress(packet.getFrom()));
		final String string = ((Message) packet).getBody();
		
		fireMessage(sender, string);
	}

	public class JabberNode implements INode
	{
		private final JabberAddress address;

		public JabberNode(final JabberAddress address)
		{
			this.address = address;
		}

		public JabberAddress getAddress()
		{
			return this.address;
		}
	}
}
