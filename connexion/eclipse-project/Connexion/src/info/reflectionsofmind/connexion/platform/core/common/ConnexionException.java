package info.reflectionsofmind.connexion.platform.core.common;

public abstract class ConnexionException extends Exception
{

	public ConnexionException()
	{
		super();
	}

	public ConnexionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConnexionException(String message)
	{
		super(message);
	}

	public ConnexionException(Throwable cause)
	{
		super(cause);
	}


}
