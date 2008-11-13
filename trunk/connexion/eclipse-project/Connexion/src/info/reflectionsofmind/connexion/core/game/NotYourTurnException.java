package info.reflectionsofmind.connexion.core.game;

public class NotYourTurnException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private final Player expectedPlayer;
	private final Player requestedPlayer;
	
	public NotYourTurnException(Player expectedPlayer, Player requestedPlayer)
	{
		this.expectedPlayer = expectedPlayer;
		this.requestedPlayer = requestedPlayer;
	}

	public Player getExpectedPlayer()
	{
		return this.expectedPlayer;
	}

	public Player getRequestedPlayer()
	{
		return this.requestedPlayer;
	}
}
