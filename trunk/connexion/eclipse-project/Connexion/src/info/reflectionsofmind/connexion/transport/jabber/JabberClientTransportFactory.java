package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.transport.IClientTransport;
import info.reflectionsofmind.connexion.transport.IClientTransportFactory;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormUtil;

public class JabberClientTransportFactory implements IClientTransportFactory
{
	private static final String FIELD_LOGIN_SETTINGS = "login-settings";
	private static final String FIELD_SERVER_ADDRESS = "server-address";

	private final IApplication application;

	public JabberClientTransportFactory(IApplication application)
	{
		this.application = application;
	}

	public IApplication getApplication()
	{
		return this.application;
	}

	@Override
	public IClientTransport createTransport(Form form) throws TransportException
	{
		final String loginSettings = FormUtil.getStringById(form, FIELD_LOGIN_SETTINGS);
		final String serverAddress = FormUtil.getStringById(form, FIELD_SERVER_ADDRESS);

		return new JabberClientTransport(loginSettings, serverAddress);
	}

	@Override
	public String getName()
	{
		return "Jabber";
	}

	@Override
	public Form getConfigurationForm()
	{
		final Form form = new Form();
		form.addField(form.new StringField(FIELD_LOGIN_SETTINGS, "Login settings", "node:password@host.domain:port/resource"));
		form.addField(form.new StringField(FIELD_SERVER_ADDRESS, "Server address", "node@host.domain/resource"));
		return form;
	}
}
