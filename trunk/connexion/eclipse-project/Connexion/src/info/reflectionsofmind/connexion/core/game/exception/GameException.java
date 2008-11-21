package info.reflectionsofmind.connexion.core.game.exception;

import info.reflectionsofmind.connexion.core.board.exception.BoardException;

public class GameException extends Exception
{
	private static final long serialVersionUID = 1L;

	public GameException()
	{
		super();
	}

	public GameException(BoardException cause)
	{
		super(cause);
	}
}
