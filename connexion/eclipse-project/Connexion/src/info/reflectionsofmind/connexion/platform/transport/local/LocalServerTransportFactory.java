package info.reflectionsofmind.connexion.platform.transport.local;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.platform.transport.TransportException;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormUtil;

public class LocalServerTransportFactory implements IServerTransportFactory
{
	private static final String FIELD_NUMBER_OF_PLAYERS = "number-of-players";

	private final IApplication application;

	public LocalServerTransportFactory(IApplication application)
	{
		this.application = application;
	}

	public IApplication getApplication()
	{
		return this.application;
	}

	@Override
	public IServerTransport createTransport(Form form) throws TransportException
	{
		return new LocalServerTransport(getApplication(), FormUtil.getIntegerById(form, FIELD_NUMBER_OF_PLAYERS));
	}

	@Override
	public String getName()
	{
		return "Local";
	}

	@Override
	public Form newConfigurationForm()
	{
		final Form form = new Form();
		form.addField(form.new IntField(FIELD_NUMBER_OF_PLAYERS, "Number of local players", 1));
		return form;
	}
}
