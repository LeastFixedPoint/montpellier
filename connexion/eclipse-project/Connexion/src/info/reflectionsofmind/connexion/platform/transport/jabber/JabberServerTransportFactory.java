package info.reflectionsofmind.connexion.platform.transport.jabber;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.platform.transport.TransportException;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormUtil;

public class JabberServerTransportFactory implements IServerTransportFactory
{
	private static final String FIELD_LOGIN_SETTINGS = "server-address";

	private final IApplication application;

	public JabberServerTransportFactory(IApplication application)
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
		final String loginSettings = FormUtil.getStringById(form, FIELD_LOGIN_SETTINGS);

		return new JabberServerTransport(loginSettings);
	}

	@Override
	public String getName()
	{
		return "Jabber";
	}

	@Override
	public Form newConfigurationForm()
	{
		final Form form = new Form();
		form.addField(form.new StringField(FIELD_LOGIN_SETTINGS, "Login settings", "connexion-server:connexion@binaryfreedom.info"));
		return form;
	}
}
