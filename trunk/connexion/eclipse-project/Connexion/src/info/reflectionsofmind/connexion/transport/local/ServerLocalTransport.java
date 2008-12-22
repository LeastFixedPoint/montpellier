package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.transport.TransportException;
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
		numberOfPlayersField = new Form.Field(FieldType.INT, "Number of players", 1);
		this.form.addField(numberOfPlayersField);
	}
	
	@Override
	public Form getForm()
	{
		return this.form;
	}

	@Override
	public void start() throws TransportException
	{
		final int number = this.numberOfPlayersField.getInt();
		
		for (int index = 0; index < number; index++)
		{
			new ClientLocalTransport(this, this.settings);
		}
	}
}
