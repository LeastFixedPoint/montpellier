package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormUtil;

public class JabberTransportFactory implements ITransportFactory
{
	@Override
	public String getName()
	{
		return "Jabber/XMPP";
	}

	@Override
	public JabberTransport createTransport(final Form form)
	{
		final String node = FormUtil.getStringById(form, "node");
		final String password = FormUtil.getStringById(form, "password");
		final String host = FormUtil.getStringById(form, "host");;
		final Integer port = FormUtil.getIntegerById(form, "port");;
		final String resource = FormUtil.getStringById(form, "resource");

		return new JabberTransport(node, host, port, password, resource);
	}

	@Override
	public Form newConnectionForm()
	{
		final Form form = new Form();
		form.addField(form.new StringField("node", "Username", ""));
		form.addField(form.new StringField("password", "Password", ""));
		form.addField(form.new StringField("host", "Hostname", ""));
		form.addField(form.new IntField("port", "Port", 5222));
		form.addField(form.new StringField("resource", "Resource", ""));
		return form;
	}
}
