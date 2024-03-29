package info.reflectionsofmind.connexion.platform.core.transport.jabber;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.core.transport.IServerToClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
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
	public IServerToClientTransport createTransport(Form form) throws TransportException
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
