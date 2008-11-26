package info.reflectionsofmind.connexion.transport.jabber;


public class JabberAddress
{
	private final String login;
	private final String password;
	private final String host;
	private final String port;
	private final String resource;
	
	public JabberAddress(final String string)
	{
		final int atPos = string.indexOf('@');
		final int slPos = string.indexOf('/');
		
		final String loginPassword = string.substring(0, atPos);
		final String hostPort = (slPos == -1) ? string.substring(atPos + 1) : string.substring(atPos + 1, slPos);
		
		this.resource = (slPos == -1) ? null : string.substring(slPos + 1);
		
		final int s1Pos = loginPassword.indexOf(':');
		
		this.login = (s1Pos == -1) ? loginPassword : loginPassword.substring(0, s1Pos);
		this.password = (s1Pos == -1) ? null : loginPassword.substring(s1Pos + 1);
		
		final int s2Pos = hostPort.indexOf(':');
		
		this.host = (s1Pos == -1) ? hostPort : hostPort.substring(0, s2Pos);
		this.port = (s1Pos == -1) ? null : hostPort.substring(s2Pos + 1);
	}
	
	public String getLogin()
	{
		return this.login;
	}
	public String getPassword()
	{
		return this.password;
	}
	public String getHost()
	{
		return this.host;
	}
	public String getPort()
	{
		return this.port;
	}
	public String getResource()
	{
		return this.resource;
	}
}
