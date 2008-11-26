package info.reflectionsofmind.connexion.local.client.exception;

public class ConnectionFailedException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ConnectionFailedException()
	{
		super();
	}

	public ConnectionFailedException(final String arg0, final Throwable arg1)
	{
		super(arg0, arg1);
	}

	public ConnectionFailedException(final String arg0)
	{
		super(arg0);
	}

	public ConnectionFailedException(final Throwable arg0)
	{
		super(arg0);
	}
}
