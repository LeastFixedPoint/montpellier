package info.reflectionsofmind.connexion.platform.core.transport.jabber;

import info.reflectionsofmind.connexion.platform.core.transport.InvalidInitStringException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JabberConnectionInfo
{
	private static final String NODE = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=]+";
	private static final String HOST = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\']+";
	private static final String PASSWORD = ".+";
	private static final String RESOURCE = "[\\w\\d\\-\\.\\_\\~\\!\\$\\(\\)\\*\\+\\,\\;\\=\\&\\'\\:]+";

	private static final Pattern INIT_STRING_PATTERN = Pattern.compile( //
			String.format("(%s):(%s)@(%s)(:\\d+)?(/(%s))?", NODE, PASSWORD, HOST, RESOURCE));

	private final String node;
	private final String password;
	private final String host;
	private final int port;
	private final String resource;

	public JabberConnectionInfo(final String node, final String password, final String host, final int port, final String resource)
	{
		this.node = node;
		this.password = password;
		this.host = host;
		this.port = port;
		this.resource = resource;
	}

	public JabberConnectionInfo(final String connectionString) throws InvalidInitStringException
	{
		final Matcher matcher = INIT_STRING_PATTERN.matcher(connectionString);
		if (!matcher.matches()) throw new InvalidInitStringException(connectionString);

		this.node = matcher.group(0);
		this.password = matcher.group(1);
		this.host = matcher.group(2);
		this.port = matcher.group(3).isEmpty() ? 5222 : Integer.parseInt(matcher.group(3));
		this.resource = matcher.group(4).isEmpty() ? null : matcher.group(4);
	}

	public String getNode()
	{
		return this.node;
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getHost()
	{
		return this.host;
	}

	public int getPort()
	{
		return this.port;
	}

	public String getResource()
	{
		return this.resource;
	}
}
