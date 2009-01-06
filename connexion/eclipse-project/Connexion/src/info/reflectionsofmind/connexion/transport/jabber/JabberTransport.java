package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.AbstractNode;
import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.Form;
import info.reflectionsofmind.connexion.util.Form.FieldType;

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

	private final Form form;

	private final Form.Field addressField;

	public JabberTransport(final JabberAddress address)
	{
		this.form = new Form();
		this.addressField = new Form.Field(FieldType.STRING, "Server's jabber ID", address.getLongString());
		this.form.addField(this.addressField);
	}

	@Override
	public Form getForm()
	{
		return this.form;
	}

	@Override
	public INode getNode(String id)
	{
		return new JabberNode(new JabberAddress(id));
	}
	
	@Override
	public void send(final INode to, final String string) throws TransportException
	{
		final Message message = new Message(((JabberNode) to).getAddress().getLongString());
		message.setBody(string);
		this.connection.sendPacket(message);
	}

	@Override
	public void start() throws TransportException
	{
		try
		{
			final JabberAddress address = new JabberAddress(this.addressField.getString());

			final ConnectionConfiguration configuration = new ConnectionConfiguration( //
					address.getHost(), address.getPort());
			SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			this.connection = new XMPPConnection(configuration);
			this.connection.connect();
			this.connection.login(address.getLogin(), address.getPassword());

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
	public void processPacket(final Packet packet)
	{
		if (!(packet instanceof Message)) return;

		final JabberNode sender = new JabberNode(new JabberAddress(packet.getFrom()));
		final String string = ((Message) packet).getBody();

		fireMessage(sender, string);
	}

	public class JabberNode extends AbstractNode
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

		@Override
		public ITransport getTransport()
		{
			return JabberTransport.this;
		}
		
		public String getId()
		{
			return getAddress().getLongString();
		}
	}
}
