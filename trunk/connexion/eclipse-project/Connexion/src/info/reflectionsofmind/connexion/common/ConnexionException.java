package info.reflectionsofmind.connexion.common;

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
