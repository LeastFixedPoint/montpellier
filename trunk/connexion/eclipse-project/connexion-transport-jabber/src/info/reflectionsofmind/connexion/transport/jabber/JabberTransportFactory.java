package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.form.Form;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JabberTransportFactory implements ITransportFactory
{
	private static final String NODE = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=]+";
	private static final String HOST = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\']+";
	private static final String PASSWORD = ".+";
	private static final String RESOURCE = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\'\\:]+";
	
	private static final Pattern INIT_STRING_PATTERN = Pattern.compile( //
			String.format("(%s):(%s)@(%s)(:\\d+)?(/(%s))?", NODE, PASSWORD, HOST, RESOURCE));
	
	@Override
	public String getName()
	{
		return "Jabber/XMPP";
	}
	
	@Override
	public JabberTransport createTransport(final String connectionString)
	{
		final Matcher matcher = INIT_STRING_PATTERN.matcher(connectionString);
		if (!matcher.matches()) throw new RuntimeException("Invalid connection string: " + connectionString);
		
		final String node = matcher.group(0);
		final String password = matcher.group(1);
		final String host = matcher.group(2);
		final Integer port = matcher.group(3).isEmpty() ? 5222 : Integer.parseInt(matcher.group(3));
		final String resource = matcher.group(4).isEmpty() ? null : matcher.group(4);
		
		return new JabberTransport(node, host, port, password, resource);
	}
	
	public Form getConnectionForm()
	{
		final Form form = new Form();
		form.addField(form.new StringField("node", "Username", ""));
		form.addField(form.new StringField("password", "Password", ""));
		form.addField(form.new StringField("host", "Hostname", ""));
		form.addField(form.new IntField("port", "Hostname", ""));
	}
}
