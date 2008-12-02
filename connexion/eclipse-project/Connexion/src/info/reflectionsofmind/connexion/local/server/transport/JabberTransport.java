package info.reflectionsofmind.connexion.local.server.transport;

import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class JabberTransport extends AbstractTransport implements PacketListener
{
	private XMPPConnection connection;

	private JabberAddress address;

	public JabberTransport(final JabberAddress address)
	{
		this.address = address;
	}

	@Override
	public void send(final ISender sender, final String string) throws TransportException
	{
		if (!(sender instanceof Sender)) throw new TransportException("Invalid sender [" + sender + "] (expected a " + Sender.class.getName() + ").");

		final Sender jabberSender = (Sender) sender;

		final Message message = new Message(jabberSender.getAddress());
		message.setBody(string);

		this.connection.sendPacket(message);
	}

	@Override
	public void start() throws TransportException
	{
		try
		{
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
	public void stop() throws TransportException
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

		final ISender sender = new Sender(packet.getFrom());
		final String string = ((Message) packet).getBody();
		
		fireMessage(sender, string);
	}

	public class Sender implements ISender
	{
		private final String address;

		public Sender(final String address)
		{
			this.address = address;
		}

		public String getAddress()
		{
			return this.address;
		}
	}
}
