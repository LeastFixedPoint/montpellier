package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport.ServerToClientNode;
import info.reflectionsofmind.connexion.util.Form;
import info.reflectionsofmind.connexion.util.Form.FieldType;

public class ServerLocalTransport extends AbstractLocalTransport
{
	private final Form form;
	private final Settings settings;
	private final Form.Field numberOfPlayersField;

	public ServerLocalTransport(final Settings settings)
	{
		this.settings = settings;
		this.form = new Form();
		this.numberOfPlayersField = new Form.Field("number-of-players", FieldType.INT, "Number of players", 1);
		this.form.addField(this.numberOfPlayersField);
	}

	@Override
	public Form getForm()
	{
		return this.form;
	}
	
	@Override
	public boolean isServerSideOnly()
	{
		return true;
	}

	@Override
	public void send(final INode to, final String message) throws TransportException
	{
		new Thread()
		{
			@Override
			public void run()
			{
				final ClientLocalTransport clientTransport = ((ServerToClientNode) to).getClientTransport();
				clientTransport.receive(clientTransport.getServerNode(), message);
			}
		}.start();
	}

	@Override
	public void start() throws TransportException
	{
		final int number = this.numberOfPlayersField.getInt();

		for (int index = 0; index < number; index++)
		{
			new ClientLocalTransport(this.settings, this, index + 1);
		}
	}
}