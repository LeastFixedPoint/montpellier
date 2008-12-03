package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.IAddressee;
import info.reflectionsofmind.connexion.transport.TransportException;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class JabberTransport extends AbstractTransport<JabberTransport.Addressee> implements PacketListener
{
	private XMPPConnection connection;

	private JabberAddress address;

	public JabberTransport(final JabberAddress address)
	{
		this.address = address;
	}

	@Override
	public void send(final Addressee sender, final String string) throws TransportException
	{
		if (!(sender instanceof Addressee)) throw new TransportException("Invalid sender [" + sender + "] (expected a " + Addressee.class.getName() + ").");

		final Addressee jabberSender = (Addressee) sender;

		final Message message = new Message(jabberSender.getAddress());
		message.setBody(string);

		this.connection.sendPacket(message);
	}

	@Override
	protected void doFirstStart() throws TransportException
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

		final Addressee sender = new Addressee(packet.getFrom());
		final String string = ((Message) packet).getBody();
		
		fireMessage(sender, string);
	}

	public class Addressee implements IAddressee
	{
		private final String address;

		public Addressee(final String address)
		{
			this.address = address;
		}

		public String getAddress()
		{
			return this.address;
		}
	}
}
