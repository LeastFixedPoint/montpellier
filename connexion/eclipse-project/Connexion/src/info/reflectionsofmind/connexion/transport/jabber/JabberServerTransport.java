package info.reflectionsofmind.connexion.transport.jabber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.reflectionsofmind.connexion.transport.AbstractServerTransport;
import info.reflectionsofmind.connexion.transport.InvalidInitStringException;
import info.reflectionsofmind.connexion.transport.TransportException;

public class JabberServerTransport extends AbstractServerTransport
{
	private static final String NODE = "[\\a\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=]+";
	private static final String HOST = "[\\a\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\']+";
	private static final String PASSWORD = ".+";
	private static final String RESOURCE = "[\\a\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\'\\:]+";

	private static final Pattern INIT_STRING_PATTERN = Pattern.compile( //
			String.format("(%s):(%s)@(%s)(/(%s))?", NODE, PASSWORD, HOST, RESOURCE));

	private final JabberCore core;

	public JabberServerTransport(String initString) throws TransportException
	{
		final Matcher matcher = INIT_STRING_PATTERN.matcher(initString);

		if (!matcher.matches()) throw new InvalidInitStringException(initString);

		final String node = matcher.group(0);
		final String password = matcher.group(1);
		final String host = matcher.group(2);
		final String resource = matcher.groupCount() > 3 ? matcher.group(4) : null;

		this.core = new JabberCore(new JabberAddress( //
				node + ":" + password + "@" + host + (resource == null ? "" : ("/" + resource))));
	}
	
	public void send(String message) throws TransportException
	{
		
	}

	@Override
	public void start() throws TransportException
	{
		this.core.start();
	}

	@Override
	public void stop()
	{
		this.core.stop();
	}
}
