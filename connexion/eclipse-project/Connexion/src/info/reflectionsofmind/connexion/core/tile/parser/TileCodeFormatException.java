package info.reflectionsofmind.connexion.core.tile.parser;

public class TileCodeFormatException extends Exception
{
	private static final long serialVersionUID = 1L;

	private final String code;

	public TileCodeFormatException(final String code)
	{
		super();
		this.code = code;
	}

	public TileCodeFormatException(final String code, final String message, final Throwable cause)
	{
		super(message, cause);
		this.code = code;
	}

	public TileCodeFormatException(final String code, final String message)
	{
		super(message);
		this.code = code;
	}

	public TileCodeFormatException(final String code, final Throwable cause)
	{
		super(cause);
		this.code = code;
	}

	public String getCode()
	{
		return this.code;
	}
	
	@Override
	public String getMessage()
	{
		return "In code [" + getCode() + "]: " + super.getMessage();
	}
}
