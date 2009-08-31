package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;

public final class JabberConnectionParameters implements IConnectionParameters
{
	private final String node, password, host, resource;
	private final Integer port;
	
	public JabberConnectionParameters(final String node, final String password, final String host,
			final String resource, final Integer port)
	{
		this.node = node;
		this.password = password;
		this.host = host;
		this.resource = resource;
		this.port = port;
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
	
	public String getResource()
	{
		return this.resource;
	}
	
	public Integer getPort()
	{
		return this.port;
	}
}
